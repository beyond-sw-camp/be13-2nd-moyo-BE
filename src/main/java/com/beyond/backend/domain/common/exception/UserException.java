package com.beyond.backend.domain.common.exception;

import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import lombok.Getter;

@Getter
public class UserException extends BaseException {
    public UserException(ExceptionMessage message) {
        super(message);
    }

    public UserException(ExceptionMessage message, String detail) {
        super(message, detail);
    }
}