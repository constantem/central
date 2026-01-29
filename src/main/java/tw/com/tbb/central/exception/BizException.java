package tw.com.tbb.central.exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
    private final String code;

    public BizException(String code) {
        super(code);
        this.code = code;
    }
}
