package tw.com.tbb.central.fx;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteRequest {
    private String cusidn;   // 身分證字號
    private String fromCurr; // 轉出幣別 (例如: TWD)
    private String toCurr;   // 轉入幣別 (例如: USD)
    private Double amount;   // 欲換算的金額
}