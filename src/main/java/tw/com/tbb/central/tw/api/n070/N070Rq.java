package tw.com.tbb.central.tw.api.n070;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class N070Rq {
    @NotBlank
    @Schema(description = "轉出帳號", example = "050100000001")
    private String outacn;//轉出帳號
    @NotBlank
    @Pattern(regexp = "\\d{3}", message = "INBNK must be 3 digits")
    @Schema(description = "轉入行庫代碼", example = "004")
    private String inbnk;//轉入行庫別
    @NotBlank
    @Schema(description = "轉入帳號", example = "004983450012")
    private String inacn;//轉入帳號
    @NotBlank
    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "AMOUNT format invalid")
    @Schema(description = "轉帳金額", example = "1000")
    private String amount;//轉帳金額
    @NotBlank
    @Schema(description = "網路銀行密碼", example = "147258")
    private String pinnew;//網路銀行密碼
}
