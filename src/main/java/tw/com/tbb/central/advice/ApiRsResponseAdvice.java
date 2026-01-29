package tw.com.tbb.central.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tw.com.tbb.central.tw.api.common.ApiRs;
import tw.com.tbb.central.tw.service.ErrorMessageService;

@RestControllerAdvice
public class ApiRsResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private ErrorMessageService errorMessageService;

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // ✅ 只攔 ApiRs (避免影響 swagger / actuator / 其他回傳型別)
        return ApiRs.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        ApiRs rs = (ApiRs) body;

        // ✅ 有 code + 沒 message → 自動補
        if (rs.getCode() != null && (rs.getMessage() == null || rs.getMessage().isBlank())) {
            rs.setMessage(errorMessageService.getMessage(rs.getCode()));
        }

        return rs;
    }
}
