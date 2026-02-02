package tw.com.tbb.central.fx;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@AllArgsConstructor
public class ErrorResponse {
    @Schema(description = "回應代碼")
    private String code;
}