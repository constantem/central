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

@RestController
@RequestMapping("/api/fx")
@Slf4j
public class FxAPI {

    @Autowired
    private FxAccountRepository repository;

    @Autowired
    private FxRateRepository rateRepository;

    @Autowired
    private FxQuoteRepository quoteRepository;

	@Autowired
    private AccountRepository twdAccountRepository;

    @PostMapping("/n510")
    public List<AccountDTO> queryBalances(@RequestBody QueryRequest request) {
        log.info("收到查詢請求 CUSIDN: {}", request.getCusidn());

        if (!"A123456814".equals(request.getCusidn())) {
            throw new IllegalArgumentException("E101");
        }

        List<FxAccountEntity> entities = repository.findAll();
        
        if (entities.isEmpty()) {
            throw new RuntimeException("E404");
        }

        return entities.stream().map(entity -> {
            List<BalanceDTO> balanceList = new ArrayList<>();
            if (entity.getBalances() != null) {
                entity.getBalances().forEach((curr, bal) -> balanceList.add(new BalanceDTO(curr, bal)));
            }
            
            return AccountDTO.builder()
                .accountNumber(entity.getAccountNumber())
                .balances(balanceList)
                .build();
        }).collect(Collectors.toList());
    }

    @PostMapping("/f007")
    public QuoteResponse getQuote(@RequestBody QuoteRequest request) {
        log.info("收到議價請求: {} -> {}", request.getFromCurr(), request.getToCurr());

        boolean isFromTwd = "TWD".equals(request.getFromCurr());
        boolean isToTwd = "TWD".equals(request.getToCurr());

        if (!isFromTwd && !isToTwd) throw new IllegalArgumentException("E102");
        
        String foreignCurr = isFromTwd ? request.getToCurr() : request.getFromCurr();
        FxRateEntity rateEntity = rateRepository.findById(foreignCurr)
                .orElseThrow(() -> new RuntimeException("E103"));

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
                .expiryTime(LocalDateTime.now().plusMinutes(10))
                .build();
        
        quoteRepository.save(quoteLog);

        return QuoteResponse.builder()
                .quoteId(quoteId)
                .rate(rate)
                .convertedAmount(convertedAmount)
                .build();
    }


	@PostMapping("/f574")
    @Transactional // 確保交易原子性，扣款失敗會全部回滾
    public Map<String, Object> executeTrade(@RequestBody Map<String, String> request) {
        String quoteId = request.get("quoteId");
        String fromAccount = request.get("fromAccount");
        String toAccount = request.get("toAccount");

        // 1. 驗證議價單
        FxQuoteEntity quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("E104"));

        if (quote.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("E105");
        }

        Double availableBalance = 0.0;

        // 2. 判斷轉出帳號扣款
        if ("TWD".equals(quote.getFromCurr())) {
            // --- 台幣轉出 (結匯) ---
            AccountEntity twdAcc = twdAccountRepository.findById(fromAccount)
                    .orElseThrow(() -> new RuntimeException("E203"));
            
            // 將 String 轉為 Double 進行運算
            Double currentBal = Double.parseDouble(twdAcc.getBalance());
            if (currentBal < quote.getAmount()) throw new RuntimeException("E204");

            Double newBal = currentBal - quote.getAmount();
            twdAcc.setBalance(String.valueOf(newBal)); // 轉回 String 存檔
            twdAccountRepository.save(twdAcc);
            availableBalance = newBal;
            
            // --- 轉入外幣帳戶 (增加餘額) ---
            FxAccountEntity toFxAcc = fxAccountRepository.findById(toAccount)
                    .orElseThrow(() -> new RuntimeException("E205"));
            Double foreignBal = toFxAcc.getBalances().getOrDefault(quote.getToCurr(), 0.0);
            toFxAcc.getBalances().put(quote.getToCurr(), foreignBal + quote.getConvertedAmount());
            fxAccountRepository.save(toFxAcc);

        } else {
            // --- 外幣轉出 (結售) ---
            FxAccountEntity fromFxAcc = fxAccountRepository.findById(fromAccount)
                    .orElseThrow(() -> new RuntimeException("E203"));
            
            Double currentForeignBal = fromFxAcc.getBalances().get(quote.getFromCurr());
            if (currentForeignBal == null || currentForeignBal < quote.getAmount()) {
                throw new RuntimeException("E204");
            }

            fromFxAcc.getBalances().put(quote.getFromCurr(), currentForeignBal - quote.getAmount());
            fxAccountRepository.save(fromFxAcc);
            availableBalance = fromFxAcc.getBalances().get(quote.getFromCurr());

            // --- 轉入台幣帳戶 (增加餘額) ---
            AccountEntity toTwdAcc = twdAccountRepository.findById(toAccount)
                    .orElseThrow(() -> new RuntimeException("E205"));
            Double currentTwdBal = Double.parseDouble(toTwdAcc.getBalance());
            toTwdAcc.setBalance(String.valueOf(currentTwdBal + quote.getConvertedAmount()));
            twdAccountRepository.save(toTwdAcc);
        }

        // 3. 封裝結果
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("tradeTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        response.put("fromAccount", fromAccount);
        response.put("fromAmount", quote.getAmount());
        response.put("fromCurr", quote.getFromCurr());
        response.put("toAccount", toAccount);
        response.put("toAmount", quote.getConvertedAmount());
        response.put("toCurr", quote.getToCurr());
        response.put("rate", quote.getRate());
        response.put("availableBalance", availableBalance);

        return response;
    }
}