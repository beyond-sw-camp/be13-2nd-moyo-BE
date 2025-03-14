package com.beyond.backend.domain.common.exception;

import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import lombok.Getter;

@Getter
public class ReportException extends BaseException {
    public ReportException(ExceptionMessage message) {
        super(message);
    }

    public ReportException(ExceptionMessage message, String detail) {
        super(message, detail);
    }
}