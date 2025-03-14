package com.beyond.backend.domain.common.exception;

import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class BaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8661014816924751645L;

    private final String type;

    private final HttpStatus status;

    public BaseException(ExceptionMessage message) {
        super(message.getMessage());

        this.type = message.name();
        this.status = message.getStatus();
    }

    public BaseException(ExceptionMessage message, String detail) {
        super(message.getMessage() + " - " + detail);
        this.type = message.name();
        this.status = message.getStatus();
    }


}