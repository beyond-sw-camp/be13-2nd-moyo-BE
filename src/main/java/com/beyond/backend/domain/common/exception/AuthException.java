package com.beyond.backend.domain.common.exception;

import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import lombok.Getter;

@Getter
public class AuthException extends BaseException {

    public AuthException(ExceptionMessage message) {
        super(message);
    }

    public AuthException(ExceptionMessage message, String detail) {
        super(message, detail);
    }
}
