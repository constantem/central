package tw.com.tbb.central.tw.api.n920;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class N920Rq {
    @NotBlank(message = "CUSIDN is required")
    @Pattern(regexp = "[A-Za-z][0-9]{9}", message = "CUSIDN format error")
    @Schema(description = "身分證統一編號", example = "A123456814")
    private String cusidn;
}
