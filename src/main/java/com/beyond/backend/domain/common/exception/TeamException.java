package com.beyond.backend.domain.common.exception;

import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import lombok.Getter;

import java.io.Serial;

@Getter
public class TeamException extends BaseException {
    @Serial
    private static final long serialVersionUID = -8661014816924751645L;

    public TeamException(ExceptionMessage message) {
        super(message);
    }

    public TeamException(ExceptionMessage message, String detail) {
        super(message, detail);
    }
}