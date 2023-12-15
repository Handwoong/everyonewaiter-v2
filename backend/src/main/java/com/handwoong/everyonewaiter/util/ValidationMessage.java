package com.handwoong.everyonewaiter.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationMessage {
    public static final String EMPTY_MESSAGE = "항목을 입력해주세요.";
    public static final String EMAIL_MESSAGE = "유효하지 않은 이메일 형식입니다.";
    public static final String PASSWORD_MESSAGE = "비밀번호는 영문, 숫자, 특수문자를 조합하여 8자 이상으로 입력해주세요.";
    public static final String PHONE_NUMBER_MESSAGE = "유효하지 않은 휴대폰 번호 형식입니다.";
}
