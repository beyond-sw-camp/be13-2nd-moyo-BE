package com.beyond.backend.domain.common.exception;

import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import lombok.Getter;

import java.io.Serial;

@Getter
public class ProjectException extends BaseException {
    @Serial
    private static final long serialVersionUID = -8661014816924751645L;

    public ProjectException(ExceptionMessage message) {
        super(message);
    }

    public ProjectException(ExceptionMessage message, String detail) {
        super(message, detail);
    }
}