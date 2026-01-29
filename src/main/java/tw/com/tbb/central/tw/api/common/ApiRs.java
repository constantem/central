package tw.com.tbb.central.tw.api.common;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiRs {
    private String code;
    private String message;

    public static ApiRs error(String errorCode) {
        return ApiRs.builder().code(errorCode).build();
    }
}
