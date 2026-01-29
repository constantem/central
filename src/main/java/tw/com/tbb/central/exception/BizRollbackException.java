package tw.com.tbb.central.exception;

import lombok.Getter;

@Getter
public class BizRollbackException extends RuntimeException {
    private final String code;
    public BizRollbackException(String code) {
        super(code);
        this.code = code;
    }
}
