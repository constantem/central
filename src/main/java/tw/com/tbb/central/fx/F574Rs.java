package tw.com.tbb.central.fx;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "F574 外幣買賣回應")
public class F574Rs extends BaseResponse {

    @Schema(description = "交易執行時間", example = "2026-02-02 10:30:00")
    private String tradeTime;

    @Schema(description = "轉出帳號", example = "050100000001")
    private String fromAccount;

    @Schema(description = "轉出金額 (原始幣別)", example = "3210.0")
    private Double fromAmount;

    @Schema(description = "轉出幣別", example = "TWD")
    private String fromCurr;

    @Schema(description = "轉入帳號", example = "010111912224")
    private String toAccount;

    @Schema(description = "轉入金額 (換算後幣別)", example = "100.0")
    private Double toAmount;

    @Schema(description = "轉入幣別", example = "USD")
    private String toCurr;

    @Schema(description = "成交匯率", example = "32.1")
    private Double rate;

    @Schema(description = "轉出帳號可用餘額", example = "6790.0")
    private Double availableBalance;
}