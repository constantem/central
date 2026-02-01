package tw.com.tbb.central.fx;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class F007Rs extends BaseResponse{
    private String quoteId;         // 議價編號 (S29FS0 + mmss)
    private Double rate;            // 套用的匯率
    private Double convertedAmount; // 換算後的金額
}