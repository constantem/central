package tw.com.tbb.central.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tw.com.tbb.central.constants.ErrorCodeConstants;
import tw.com.tbb.central.domain.ApiError;
import tw.com.tbb.central.tw.api.TwApi;
import tw.com.tbb.central.tw.api.common.ApiRs;
import tw.com.tbb.central.validation.ValidationErrorCodeMapper;

@RestControllerAdvice(assignableTypes = {
        TwApi.class
})
@RequiredArgsConstructor
public class ApiExceptionHandler {
    private final ValidationErrorCodeMapper mapper;

    // @Valid @RequestBody 驗證錯誤
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiRs handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ApiError err = mapper.toError(ex.getBindingResult());
        return ApiRs.builder()
                .code(err.getCode())
                .message(err.getMessage())
                .build();
    }

    // Request JSON 解析失敗
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiRs handleNotReadable(HttpMessageNotReadableException ex) {
        return ApiRs.error(ErrorCodeConstants.JSON_ERROR);//請求 JSON 反序列化失敗
    }

    // 商業邏輯錯誤
    @ExceptionHandler(BizException.class)
    public ApiRs handleBiz(BizException ex) {
        return ApiRs.error(ex.getCode());
    }

    // 兜底：沒預期的錯誤
    @ExceptionHandler(Exception.class)
    public ApiRs handleAny(Exception ex) {
        return ApiRs.error(ErrorCodeConstants.SYS_ERROR);//系統錯誤
    }
}
