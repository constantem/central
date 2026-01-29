package tw.com.tbb.central.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tw.com.tbb.central.domain.ApiError;
import tw.com.tbb.central.tw.service.ErrorMessageService;

@Component
public class ErrorFactory {


    private static ErrorMessageService errorMessageService;
    @Autowired
    public void setErrorMessageService(ErrorMessageService errorMessageService) {
        ErrorFactory.errorMessageService = errorMessageService;
    }

    public static String getMessage(String code) {
        return errorMessageService.getMessage(code);
    }

    public static ApiError create(String code) {
        return ApiError.builder()
                .code(code)
                .message(errorMessageService.getMessage(code))
                .build();
    }
}
