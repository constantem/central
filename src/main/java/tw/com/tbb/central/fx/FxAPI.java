package tw.com.tbb.central.fx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import tw.com.tbb.central.tw.repository.AccountRepository;
import tw.com.tbb.central.tw.entity.AccountEntity;
import org.springframework.transaction.annotation.Transactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fx")
@Slf4j
@Tag(name = "FX API", description = "外幣電文 - 中心主機模擬")
public class FxAPI {

    @Autowired
    private FxAccountRepository fxAccountRepository;

    @Autowired
    private FxRateRepository rateRepository;

    @Autowired
    private FxQuoteRepository quoteRepository;

    @Autowired
    private AccountRepository twdAccountRepository;

    @Value("${password}")
    private String password;

    @Operation(summary = "N510 自行外幣餘額查詢")
    @PostMapping("/n510")
    public N510Rs queryBalances(@Valid @RequestBody N510Rq request) {
        if (!"A123456814".equals(request.getCusidn())) {
            throw new IllegalArgumentException("E101");
        }

        List<FxAccountEntity> entities = fxAccountRepository.findAll();
        if (entities.isEmpty()) throw new IllegalArgumentException("E107");

        List<AccountDTO> accountList = entities.stream().map(entity -> {
            List<BalanceDTO> balanceList = new ArrayList<>();
            if (entity.getBalances() != null) {
                entity.getBalances().forEach((curr, bal) -> balanceList.add(new BalanceDTO(curr, bal)));
            }
            return AccountDTO.builder()
                .accountNumber(entity.getAccountNumber())
                .balances(balanceList)
                .build();
        }).collect(Collectors.toList());

        return N510Rs.builder().accountList(accountList).build();
    }

    @Operation(summary = "F007 匯率議價 (提前檢核餘額)")
    @PostMapping("/f007")
    public F007Rs getQuote(@Valid @RequestBody F007Rq request) {
        log.info("--- [F007] 議價餘額檢核 ---");
        
        boolean isFromTwd = "TWD".equals(request.getFromCurr());
        boolean isToTwd = "TWD".equals(request.getToCurr());
        if (isFromTwd == isToTwd) throw new IllegalArgumentException("E102"); 

        if (isFromTwd) {
            AccountEntity twdAcc = twdAccountRepository.findById(request.getFromAccount())
                    .orElseThrow(() -> new IllegalArgumentException("E107"));
            if (parseDoubleSafe(twdAcc.getBalance()) < request.getAmount()) throw new IllegalArgumentException("E106");
        } else {
            FxAccountEntity fxAcc = fxAccountRepository.findById(request.getFromAccount())
                    .orElseThrow(() -> new IllegalArgumentException("E107"));
            Double foreignBal = fxAcc.getBalances().getOrDefault(request.getFromCurr(), 0.0);
            if (foreignBal < request.getAmount()) throw new IllegalArgumentException("E106");
        }

        String foreignCurr = isFromTwd ? request.getToCurr() : request.getFromCurr();
        FxRateEntity rateEntity = rateRepository.findById(foreignCurr)
                .orElseThrow(() -> new IllegalArgumentException("E103"));

        double rate = isFromTwd ? rateEntity.getSellRate() : rateEntity.getBuyRate();
        double convertedAmount = isFromTwd ? (request.getAmount() / rate) : (request.getAmount() * rate);

        String mmss = LocalDateTime.now().format(DateTimeFormatter.ofPattern("mmss"));
        String quoteId = "S29FS0" + mmss;

        FxQuoteEntity quoteLog = FxQuoteEntity.builder()
                .quoteId(quoteId)
                .cusidn(request.getCusidn())
                .fromCurr(request.getFromCurr())
                .toCurr(request.getToCurr())
                .amount(request.getAmount())
                .convertedAmount(convertedAmount)
                .rate(rate)
                .expiryTime(LocalDateTime.now().plusSeconds(35))
                .build();
        
        quoteRepository.save(quoteLog);

        return F007Rs.builder()
                .quoteId(quoteId)
                .rate(rate)
                .convertedAmount(convertedAmount)
                .build();
    }

    @Operation(summary = "F574 外幣買賣執行 (含帳務終端檢核)")
    @PostMapping("/f574")
    @Transactional 
    public F574Rs executeTrade(@Valid @RequestBody F574Rq request) {
        log.info("--- [F574] 主機帳務檢核與扣款 ---");

        if (!password.equals(request.getPinnew())) {
            throw new IllegalArgumentException("E108");
        }

        try {
            FxQuoteEntity quote = quoteRepository.findById(request.getQuoteId())
                    .orElseThrow(() -> new IllegalArgumentException("E104"));

            if (quote.getExpiryTime().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("E105");
            }

            Double tradeAmount = quote.getAmount();
            Double convertedAmount = quote.getConvertedAmount();
            Double availBal = 0.0;

            // 3. 執行帳務更新 (檢核轉出入帳號與餘額)
            if ("TWD".equals(quote.getFromCurr())) {
                // 【結匯】轉出(台幣) -> 轉入(外幣)
                AccountEntity twd = twdAccountRepository.findById(request.getFromAccount())
                        .orElseThrow(() -> new IllegalArgumentException("E107")); // 轉出台幣帳號不存在
                
                FxAccountEntity fx = fxAccountRepository.findById(request.getToAccount())
                        .orElseThrow(() -> new IllegalArgumentException("E109")); // 轉入外幣帳號不存在或異常
                
                Double currentTwdBal = parseDoubleSafe(twd.getBalance());
                if (currentTwdBal < tradeAmount) throw new IllegalArgumentException("E106"); // 餘額不足

                // 更新餘額
                Double newTwdBal = currentTwdBal - tradeAmount;
                twd.setBalance(String.format("%.2f", newTwdBal));
                twdAccountRepository.save(twd);
                availBal = newTwdBal;
                
                Double currentFxBal = fx.getBalances().getOrDefault(quote.getToCurr(), 0.0);
                fx.getBalances().put(quote.getToCurr(), currentFxBal + convertedAmount);
                fxAccountRepository.save(fx);

            } else {
                // 【結售】轉出(外幣) -> 轉入(台幣)
                FxAccountEntity fx = fxAccountRepository.findById(request.getFromAccount())
                        .orElseThrow(() -> new IllegalArgumentException("E107")); // 轉出外幣帳號不存在
                
                AccountEntity twd = twdAccountRepository.findById(request.getToAccount())
                        .orElseThrow(() -> new IllegalArgumentException("E109")); // 轉入台幣帳號不存在或異常

                Double currentFxBal = fx.getBalances().getOrDefault(quote.getFromCurr(), 0.0);
                if (currentFxBal < tradeAmount) throw new IllegalArgumentException("E106"); // 餘額不足

                // 更新餘額
                fx.getBalances().put(quote.getFromCurr(), currentFxBal - tradeAmount);
                fxAccountRepository.save(fx);
                availBal = fx.getBalances().get(quote.getFromCurr());

                Double newTwdBal = parseDoubleSafe(twd.getBalance()) + convertedAmount;
                twd.setBalance(String.format("%.2f", newTwdBal));
                twdAccountRepository.save(twd);
            }

            return F574Rs.builder()
                    .tradeTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .fromAccount(request.getFromAccount())
                    .fromAmount(tradeAmount)
                    .fromCurr(quote.getFromCurr())
                    .toAccount(request.getToAccount())
                    .toAmount(convertedAmount)
                    .toCurr(quote.getToCurr())
                    .rate(quote.getRate())
                    .availableBalance(availBal)
                    .build();

        } catch (IllegalArgumentException e) {
            // 直接拋出業務錯誤碼
            throw e;
        } catch (Exception e) {
            log.error("系統異常:", e);
            throw new IllegalArgumentException("E999");
        }
    }

    private Double parseDoubleSafe(String val) {
        if (val == null || val.trim().isEmpty()) return 0.0;
        return Double.parseDouble(val.replace(",", ""));
    }
}