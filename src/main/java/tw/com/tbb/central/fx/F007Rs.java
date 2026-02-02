package tw.com.tbb.central.fx;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.experimental.SuperBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "F007匯率確認回應")
public class F007Rs extends BaseResponse{
    @Schema(description = "議價編號", example = "S29FS01234")
    private String quoteId;         // 議價編號 (S29FS0 + mmss)
    @Schema(description = "匯率", example = "31.5341")
    private Double rate;            // 套用的匯率
    @Schema(description = "換算後金額", example = "1000.0")
    private Double convertedAmount; // 換算後的金額
}