package org.example.clinicjava.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidateException extends RuntimeException {

    private final int errorCode;
    private final Object data;

    public ValidateException(String message) {
        super(message);
        this.errorCode = StatusCode.BAD_REQUEST_BE_TRANSLATED.getCode();
        this.data = null;
    }

    public ValidateException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.data = null;
    }

    public ValidateException(String message, int errorCode, Object data) {
        super(message);
        this.errorCode = errorCode;
        this.data = data;
    }
}
