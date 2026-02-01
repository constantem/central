package tw.com.tbb.central.tw.api.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiRs {
    @Schema(description = "回應代碼")
    private String code;
    @Schema(description = "回應訊息")
    private String message;

    public static ApiRs error(String errorCode) {
        return ApiRs.builder().code(errorCode).build();
    }
}
