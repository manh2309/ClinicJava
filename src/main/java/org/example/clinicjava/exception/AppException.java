package org.example.clinicjava.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {
    public AppException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode ;
    }
    public AppException(StatusCode statusCode, String mess) {
        super(mess);
        this.statusCode = statusCode ;
    }

    private StatusCode statusCode;
}
