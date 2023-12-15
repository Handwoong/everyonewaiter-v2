package com.handwoong.everyonewaiter.exception;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ErrorResponse(
        String name,
        String message,
        int status,
        String code,
        LocalDateTime timestamp
) {
    public static ErrorResponse from(final ErrorCode errorCode) {
        return create(errorCode, errorCode.getMessage());
    }

    public static ErrorResponse of(final ErrorCode errorCode, final String message) {
        return create(errorCode, message);
    }

    private static ErrorResponse create(final ErrorCode errorCode, final String message) {
        return ErrorResponse.builder()
                .name(errorCode.getStatus().name())
                .message(message)
                .status(errorCode.getStatus().value())
                .code(errorCode.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
