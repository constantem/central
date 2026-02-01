package tw.com.tbb.central.fx;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "外幣買賣交易請求")
public class F574Rq {
    @NotBlank@Schema(description = "議價編號", example = "S29FS01234")
    private String quoteId;

    @NotBlank@Schema(description = "轉出帳號", example = "010111912224")
    private String fromAccount;

    @NotBlank@Schema(description = "轉入帳號", example = "010111912224")
    private String toAccount;

    @NotBlank@Schema(description = "交易密碼/PIN", example = "123456")
    private String pinnew;
}