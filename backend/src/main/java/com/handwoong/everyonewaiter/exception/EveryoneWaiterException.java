package com.handwoong.everyonewaiter.exception;

import lombok.Getter;

@Getter
public class EveryoneWaiterException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String field;

    public EveryoneWaiterException() {
        super(ErrorCode.SERVER_ERROR.getMessage());
        this.errorCode = ErrorCode.SERVER_ERROR;
        this.field = "";
    }

    public EveryoneWaiterException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.field = "";
    }

    public EveryoneWaiterException(final ErrorCode errorCode, final String field) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.field = field;
    }

    public EveryoneWaiterException(final ErrorCode errorCode, final String field, final Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.field = field;
    }
}
