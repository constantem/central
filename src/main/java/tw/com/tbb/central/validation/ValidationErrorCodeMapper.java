package tw.com.tbb.central.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import tw.com.tbb.central.constants.ErrorCodeConstants;
import tw.com.tbb.central.domain.ApiError;
import tw.com.tbb.central.tw.service.ErrorMessageService;

import java.util.Map;

@Component
public class ValidationErrorCodeMapper {

    @Autowired
    private ErrorMessageService errorMessageService;

    // constraint -> error code
    private static final Map<String, String> CONSTRAINT_TO_CODE = Map.of(
            "NotBlank", ErrorCodeConstants.E200,
            "NotNull",  ErrorCodeConstants.E201,
            "Pattern",  ErrorCodeConstants.E202
    );

    public ApiError toError(BindingResult br) {
        String constraint = firstConstraint(br);
        String code = CONSTRAINT_TO_CODE.getOrDefault(constraint, ErrorCodeConstants.SYS_ERROR);
        return ApiError.builder()
                .code(code)
                .message(errorMessageService.getMessage(code))
                .build();
    }

    private String firstConstraint(BindingResult br) {
        FieldError fe = br.getFieldErrors().isEmpty() ? null : br.getFieldErrors().get(0);
        return fe == null ? null : fe.getCode();
    }
}
