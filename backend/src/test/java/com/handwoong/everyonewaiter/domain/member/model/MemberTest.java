package com.handwoong.everyonewaiter.domain.member.model;

import static com.handwoong.everyonewaiter.util.Constants.NEW_EMAIL;
import static com.handwoong.everyonewaiter.util.Constants.NEW_PASSWORD;
import static com.handwoong.everyonewaiter.util.Constants.NEW_PHONE_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.handwoong.everyonewaiter.domain.member.dto.MemberRegisterRequest;
import com.handwoong.everyonewaiter.exception.ErrorCode;
import com.handwoong.everyonewaiter.exception.EveryoneWaiterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.password.PasswordEncoder;

class MemberTest {
    private static final PasswordEncoder passwordEncoder = new FakePasswordEncoder();

    private Member member;

    @BeforeEach
    void setUp() {
        final MemberRegisterRequest request = new MemberRegisterRequest(NEW_EMAIL, NEW_PASSWORD, NEW_PHONE_NUMBER);
        member = Member.create(request, passwordEncoder);
    }

    @Test
    @DisplayName("회원을 생성한다.")
    void create() {
        // given
        final MemberRegisterRequest request = new MemberRegisterRequest(NEW_EMAIL, NEW_PASSWORD, NEW_PHONE_NUMBER);

        // when
        final Member member = Member.create(request, passwordEncoder);

        // then
        final Email email = new Email(NEW_EMAIL);
        final Password password = new Password(NEW_PASSWORD + "encode");
        final PhoneNumber phoneNumber = new PhoneNumber(NEW_PHONE_NUMBER);
        assertThat(member).extracting("email").isEqualTo(email);
        assertThat(member).extracting("password").isEqualTo(password);
        assertThat(member).extracting("phoneNumber").isEqualTo(phoneNumber);
        assertThat(member).extracting("status").isEqualTo(MemberStatus.INACTIVE);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"username", "username@domain"})
    @DisplayName("이메일 형식이 옳바르지 않으면 회원 생성에 실패한다.")
    void createInvalidEmailFormat(final String email) {
        // given
        final MemberRegisterRequest request = new MemberRegisterRequest(email, NEW_PASSWORD, NEW_PHONE_NUMBER);

        // expect
        assertThatThrownBy(() -> Member.create(request, passwordEncoder))
                .isInstanceOf(EveryoneWaiterException.class)
                .hasMessage(ErrorCode.INVALID_EMAIL.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"password", "password1234"})
    @DisplayName("비밀번호 형식이 옳바르지 않으면 회원 생성에 실패한다.")
    void createInvalidPasswordFormat(final String password) {
        // given
        final MemberRegisterRequest request = new MemberRegisterRequest(NEW_EMAIL, password, NEW_PHONE_NUMBER);

        // expect
        assertThatThrownBy(() -> Member.create(request, passwordEncoder))
                .isInstanceOf(EveryoneWaiterException.class)
                .hasMessage(ErrorCode.INVALID_PASSWORD.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"010-1234-5678", "00122223333"})
    @DisplayName("휴대폰 번호 형식이 옳바르지 않으면 회원 생성에 실패한다.")
    void createInvalidPhoneNumberFormat(final String phoneNumber) {
        // given
        final MemberRegisterRequest request = new MemberRegisterRequest(NEW_EMAIL, NEW_PASSWORD, phoneNumber);

        // expect
        assertThatThrownBy(() -> Member.create(request, passwordEncoder))
                .isInstanceOf(EveryoneWaiterException.class)
                .hasMessage(ErrorCode.INVALID_PHONE_NUMBER.getMessage());
    }

    @ParameterizedTest
    @EnumSource
    @DisplayName("회원 상태를 변경한다.")
    void changeStatus(final MemberStatus status) {
        // given
        // when
        member.changeStatus(status);

        // then
        assertThat(member).extracting("status").isEqualTo(status);
    }
}
