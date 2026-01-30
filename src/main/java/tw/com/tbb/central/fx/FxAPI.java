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
    private FxAccountRepository fxAccountRepository;

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

        List<FxAccountEntity> entities = fxAccountRepository.findAll();
        
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
    @Transactional 
    public Map<String, Object> executeTrade(@RequestBody Map<String, String> request) {
        String quoteId = request.get("quoteId");
        String fromAccount = request.get("fromAccount");
        String toAccount = request.get("toAccount");

        log.info("--- 交易開始 --- QuoteId: {}, From: {}, To: {}", quoteId, fromAccount, toAccount);

        try {
            // 1. 驗證議價單是否存在
            FxQuoteEntity quote = quoteRepository.findById(quoteId)
                    .orElseThrow(() -> new RuntimeException("E104"));

            // 2. 檢查過期
            if (quote.getExpiryTime() == null || quote.getExpiryTime().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("E105");
            }

            Double tradeAmount = quote.getAmount();
            Double convertedAmount = quote.getConvertedAmount();
            Double availableBalance = 0.0;

            // 3. 處理台幣或外幣扣款邏輯
            if ("TWD".equals(quote.getFromCurr())) {
                // 【結匯邏輯：台幣 -> 外幣】
                AccountEntity twdAcc = twdAccountRepository.findById(fromAccount)
                        .orElseThrow(() -> new RuntimeException("E203"));
                
                Double currentBal = parseDoubleSafe(twdAcc.getBalance());
                if (currentBal < tradeAmount) throw new RuntimeException("E204");

                // 更新台幣帳戶
                Double newBal = currentBal - tradeAmount;
                twdAcc.setBalance(String.format("%.2f", newBal)); 
                twdAccountRepository.save(twdAcc);
                availableBalance = newBal;
                
                // 更新外幣帳戶 (增加餘額)
                FxAccountEntity toFxAcc = fxAccountRepository.findById(toAccount)
                        .orElseThrow(() -> new RuntimeException("E205"));
                
                // 防禦：如果資料庫抓出來的 Map 是空的，要手動初始化
                if (toFxAcc.getBalances() == null) {
                    toFxAcc.setBalances(new HashMap<>());
                }
                
                Double foreignBal = toFxAcc.getBalances().getOrDefault(quote.getToCurr(), 0.0);
                toFxAcc.getBalances().put(quote.getToCurr(), foreignBal + convertedAmount);
                fxAccountRepository.save(toFxAcc);

            } else {
                // 【結售邏輯：外幣 -> 台幣】
                FxAccountEntity fromFxAcc = fxAccountRepository.findById(fromAccount)
                        .orElseThrow(() -> new RuntimeException("E203"));
                
                if (fromFxAcc.getBalances() == null) throw new RuntimeException("E203");
                
                Double currentForeignBal = fromFxAcc.getBalances().getOrDefault(quote.getFromCurr(), 0.0);
                if (currentForeignBal < tradeAmount) throw new RuntimeException("E204");

                // 更新外幣帳戶 (扣除餘額)
                fromFxAcc.getBalances().put(quote.getFromCurr(), currentForeignBal - tradeAmount);
                fxAccountRepository.save(fromFxAcc);
                availableBalance = fromFxAcc.getBalances().get(quote.getFromCurr());

                // 更新台幣帳戶 (增加餘額)
                AccountEntity toTwdAcc = twdAccountRepository.findById(toAccount)
                        .orElseThrow(() -> new RuntimeException("E205"));
                Double currentTwdBal = parseDoubleSafe(toTwdAcc.getBalance());
                Double newTwdBal = currentTwdBal + convertedAmount;
                toTwdAcc.setBalance(String.format("%.2f", newTwdBal));
                twdAccountRepository.save(toTwdAcc);
            }

            // 4. 回傳成功結果 (使用 LinkedHashMap 確保順序)
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("tradeTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            response.put("fromAccount", fromAccount);
            response.put("fromAmount", tradeAmount);
            response.put("fromCurr", quote.getFromCurr());
            response.put("toAccount", toAccount);
            response.put("toAmount", convertedAmount);
            response.put("toCurr", quote.getToCurr());
            response.put("rate", quote.getRate());
            response.put("availableBalance", availableBalance);

            log.info("--- 交易成功 --- 結算餘額: {}", availableBalance);
            return response;

        } catch (RuntimeException e) {
            // 如果是我們定義的 E 開頭錯誤，直接丟給 ExceptionHandler
            if (e.getMessage() != null && e.getMessage().startsWith("E")) {
                log.warn("交易業務檢核失敗: {}", e.getMessage());
                throw e;
            }
            log.error("交易執行發生非預期 RuntimeException:", e);
            throw new RuntimeException("E999");
        } catch (Exception e) {
            log.error("交易執行發生系統級 Exception:", e);
            throw new RuntimeException("E999");
        }
    }

    private Double parseDoubleSafe(String val) {
        try {
            if (val == null || val.trim().isEmpty()) return 0.0;
            return Double.parseDouble(val.replace(",", ""));
        } catch (NumberFormatException e) {
            log.error("無法將字串轉換為金額: [{}]", val);
            throw new RuntimeException("E999");
        }
    }
}