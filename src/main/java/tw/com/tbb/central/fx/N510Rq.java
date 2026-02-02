package tw.com.tbb.central.fx;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
@Data
@Schema(description = "N510外幣帳戶餘額請求")
public class N510Rq {
    @NotBlank(message = "CUSIDN is required")
    @Pattern(regexp = "[A-Za-z][0-9]{9}", message = "CUSIDN format error")
    @Schema(description = "身分證統一編號", example = "A123456814")
    private String cusidn;
}