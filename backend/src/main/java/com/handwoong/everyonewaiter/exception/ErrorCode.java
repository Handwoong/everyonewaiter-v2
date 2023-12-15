package com.handwoong.everyonewaiter.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * 400 BAD REQUEST
     */
    METHOD_ARGUMENT_NOT_VALID(BAD_REQUEST, "요청 인자가 잘못되었습니다."),
    INVALID_CLAIM_KEY(BAD_REQUEST, "유효하지 않은 클레임 key 입니다."),
    INVALID_EMAIL(BAD_REQUEST, "유효하지 않은 이메일 입니다."),
    INVALID_PASSWORD(BAD_REQUEST, "비밀번호는 영문, 숫자, 특수문자를 조합하여 8자 이상이어야 합니다."),
    INVALID_PHONE_NUMBER(BAD_REQUEST, "유효하지 않은 휴대폰 번호 형식입니다."),
    EXISTS_EMAIL(BAD_REQUEST, "이미 존재하는 이메일 입니다."),
    EXISTS_PHONE_NUMBER(BAD_REQUEST, "이미 존재하는 휴대폰 번호 입니다."),

    /**
     * 401 UNAUTHORIZED
     */
    UN_AUTHORIZE(UNAUTHORIZED, "인증에 실패하였습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_MEMBER(UNAUTHORIZED, "계정이 휴면 상태입니다."),
    NEED_VERIFY_EMAIL(UNAUTHORIZED, "이메일 인증이 완료되지 않았습니다."),
    LOCKED_MEMBER(UNAUTHORIZED, "계정이 잠금 상태입니다."),
    LEAVE_MEMBER(UNAUTHORIZED, "탈퇴된 계정입니다."),

    /**
     * 403 FORBIDDEN
     */
    ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),

    /**
     * 404 NOT FOUND
     */
    NOT_FOUND_MEMBER(NOT_FOUND, "해당 회원을 찾을 수 없습니다."),

    /**
     * 405 METHOD NOT ALLOWED
     */
    NOT_SUPPORT_METHOD(METHOD_NOT_ALLOWED, "지원하지 않는 요청입니다."),

    /**
     * 500 INTERNAL SERVER ERROR
     */
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버에 문제가 발생하였습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    public String statusName() {
        return status.name();
    }

    public int statusCode() {
        return status.value();
    }
}
