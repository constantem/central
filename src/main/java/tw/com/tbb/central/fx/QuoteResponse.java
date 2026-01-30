package tw.com.tbb.central.fx;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteResponse extends BaseResponse{
    private String quoteId;         // 議價編號 (S29FS0 + mmss)
    private Double rate;            // 套用的匯率
    private Double convertedAmount; // 換算後的金額
}