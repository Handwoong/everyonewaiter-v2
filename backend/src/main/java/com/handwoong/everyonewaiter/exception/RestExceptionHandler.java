package com.handwoong.everyonewaiter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {
    private static final String MESSAGE_FORMAT = "[%s] %s %s";
    private static final String VALIDATION_MESSAGE_FORMAT = "\n%s-[%s]-[%s]";

    @ExceptionHandler({EveryoneWaiterException.class})
    public ResponseEntity<ErrorResponse> everyoneWaiter(final EveryoneWaiterException exception) {
        final ErrorCode errorCode = exception.getErrorCode();
        logWarn(errorCode, exception, exception.getField());
        return createErrorResponse(errorCode);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> methodArgumentNotValid(final MethodArgumentNotValidException exception) {
        final ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_NOT_VALID;
        final String logMessage = createValidationLogMessage(exception);
        final String errorMessage = createValidationErrorMessage(exception);
        logWarn(errorCode, exception, logMessage);
        return createErrorResponse(errorCode, errorMessage);
    }

    @ExceptionHandler({AccountExpiredException.class})
    public ResponseEntity<ErrorResponse> accountExpired(final AccountExpiredException exception) {
        final ErrorCode errorCode = ErrorCode.EXPIRED_MEMBER;
        logWarn(errorCode, exception);
        return createErrorResponse(errorCode);
    }

    @ExceptionHandler({LockedException.class})
    public ResponseEntity<ErrorResponse> locked(final LockedException exception) {
        final ErrorCode errorCode = ErrorCode.LOCKED_MEMBER;
        logWarn(errorCode, exception);
        return createErrorResponse(errorCode);
    }

    @ExceptionHandler({CredentialsExpiredException.class})
    public ResponseEntity<ErrorResponse> credentialsExpired(final CredentialsExpiredException exception) {
        final ErrorCode errorCode = ErrorCode.NEED_VERIFY_EMAIL;
        logWarn(errorCode, exception);
        return createErrorResponse(errorCode);
    }

    @ExceptionHandler({DisabledException.class})
    public ResponseEntity<ErrorResponse> disabled(final DisabledException exception) {
        final ErrorCode errorCode = ErrorCode.LEAVE_MEMBER;
        logWarn(errorCode, exception);
        return createErrorResponse(errorCode);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> badCredentials(final BadCredentialsException exception) {
        final ErrorCode errorCode = ErrorCode.CHECK_EMAIL_AND_PASSWORD;
        logWarn(errorCode, exception);
        return createErrorResponse(errorCode);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> usernameNotFound(final UsernameNotFoundException exception) {
        final ErrorCode errorCode = ErrorCode.INVALID_EMAIL;
        logWarn(errorCode, exception, exception.getMessage());
        return createErrorResponse(errorCode);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponse> methodNotSupported(final HttpRequestMethodNotSupportedException exception) {
        final ErrorCode errorCode = ErrorCode.NOT_SUPPORT_METHOD;
        logWarn(errorCode, exception);
        return createErrorResponse(errorCode);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> exception(final Exception exception) {
        final ErrorCode errorCode = ErrorCode.SERVER_ERROR;
        logError(errorCode, exception);
        return createErrorResponse(errorCode);
    }

    private void logWarn(final ErrorCode errorCode, final Exception exception) {
        final String logMessage = String.format(
                MESSAGE_FORMAT, errorCode.name(), errorCode.getStatus(), errorCode.getMessage());
        log.warn(logMessage, exception);
    }

    private void logWarn(final ErrorCode errorCode, final Exception exception, final String field) {
        final String logMessage = String.format(
                MESSAGE_FORMAT, errorCode.name(), errorCode.getStatus(), errorCode.getMessage() + "-" + field);
        log.warn(logMessage, exception);
    }

    private void logError(final ErrorCode errorCode, final Exception exception) {
        final String logMessage = String.format(
                MESSAGE_FORMAT, errorCode.name(), errorCode.getStatus(), errorCode.getMessage());
        log.error(logMessage, exception);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(final ErrorCode errorCode) {
        final ErrorResponse errorResponse = ErrorResponse.from(errorCode);
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(final ErrorCode errorCode, final String errorMessage) {
        final ErrorResponse errorResponse = ErrorResponse.of(errorCode, errorMessage);
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    private String createValidationErrorMessage(final MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
    }

    private String createValidationLogMessage(final MethodArgumentNotValidException exception) {
        final StringBuilder sb = new StringBuilder();
        final BindingResult bindingResult = exception.getBindingResult();
        for (final FieldError fieldError : bindingResult.getFieldErrors()) {
            final String message = String.format(
                    VALIDATION_MESSAGE_FORMAT,
                    fieldError.getDefaultMessage(),
                    fieldError.getField(),
                    fieldError.getRejectedValue());
            sb.append(message);
        }
        return sb.toString();
    }
}
