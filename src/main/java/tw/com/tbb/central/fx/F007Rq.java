package tw.com.tbb.central.fx;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "F007匯率確認請求")
public class F007Rq {

    @NotBlank(message = "CUSIDN is required")
    @Pattern(regexp = "[A-Za-z][0-9]{9}", message = "CUSIDN format error")
    @Schema(description = "身分證統一編號", example = "A123456814")
    private String cusidn;

    @NotBlank(message = "轉出帳號不能為空")
    @Schema(description = "轉出帳號 (用來提前檢核餘額)", example = "999999999")
    private String fromAccount; 

    @NotBlank
    @Schema(description = "轉出幣別", example = "TWD")
    private String fromCurr;

    @NotBlank
    @Schema(description = "轉入幣別", example = "USD")
    private String toCurr;

    @NotNull(message = "金額不能為空")
    @Schema(description = "交易金額", example = "10.0")
    private Double amount;
}