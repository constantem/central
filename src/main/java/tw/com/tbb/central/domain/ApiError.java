package tw.com.tbb.central.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ApiError {
    private String code;
    private String message;
}
