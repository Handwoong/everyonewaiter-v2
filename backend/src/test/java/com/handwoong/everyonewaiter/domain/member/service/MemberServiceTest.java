package com.handwoong.everyonewaiter.domain.member.service;

import static com.handwoong.everyonewaiter.util.Constants.NEW_EMAIL;
import static com.handwoong.everyonewaiter.util.Constants.NEW_PASSWORD;
import static com.handwoong.everyonewaiter.util.Constants.NEW_PHONE_NUMBER;
import static com.handwoong.everyonewaiter.util.Constants.USER_EMAIL;
import static com.handwoong.everyonewaiter.util.Constants.USER_PASSWORD;
import static com.handwoong.everyonewaiter.util.Constants.USER_PHONE_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.handwoong.everyonewaiter.config.security.jwt.GrantType;
import com.handwoong.everyonewaiter.config.security.jwt.TokenResponse;
import com.handwoong.everyonewaiter.domain.member.dto.MemberLoginRequest;
import com.handwoong.everyonewaiter.domain.member.dto.MemberRegisterRequest;
import com.handwoong.everyonewaiter.domain.member.model.Email;
import com.handwoong.everyonewaiter.domain.member.model.Member;
import com.handwoong.everyonewaiter.domain.member.model.MemberStatus;
import com.handwoong.everyonewaiter.domain.member.repository.MemberRepository;
import com.handwoong.everyonewaiter.exception.ErrorCode;
import com.handwoong.everyonewaiter.exception.EveryoneWaiterException;
import com.handwoong.everyonewaiter.util.DatabaseCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class MemberServiceTest {
    private static final MemberRegisterRequest registerRequest =
            new MemberRegisterRequest(USER_EMAIL, USER_PASSWORD, USER_PHONE_NUMBER);
    private static final MemberLoginRequest loginRequest = new MemberLoginRequest(USER_EMAIL, USER_PASSWORD);

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        final Member member = Member.create(registerRequest, passwordEncoder);
        member.changeStatus(MemberStatus.ACTIVE);
        memberRepository.save(member);
    }

    @AfterEach
    void clear() {
        databaseCleaner.execute();
    }

    @Test
    @DisplayName("회원가입 로직이 정상 동작한다.")
    void register() {
        // given
        final Email email = new Email(NEW_EMAIL);
        final MemberRegisterRequest request = new MemberRegisterRequest(NEW_EMAIL, NEW_PASSWORD, NEW_PHONE_NUMBER);

        // when
        memberService.register(request);

        // then
        final Member member = memberRepository.findByEmail(email).orElseThrow();
        assertThat(member).extracting("email").isEqualTo(email);
    }

    @Test
    @DisplayName("중복된 이메일이 존재하면 회원가입에 실패한다.")
    void registerDuplicateEmail() {
        // expect
        assertThatThrownBy(() -> memberService.register(registerRequest))
                .isInstanceOf(EveryoneWaiterException.class)
                .hasMessage(ErrorCode.EXISTS_EMAIL.getMessage());
    }

    @Test
    @DisplayName("중복된 휴대폰 번호가 존재하면 회원가입에 실패한다.")
    void registerDuplicatePhoneNumber() {
        // given
        final MemberRegisterRequest request = new MemberRegisterRequest(NEW_EMAIL, NEW_PASSWORD, USER_PHONE_NUMBER);

        // expect
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(EveryoneWaiterException.class)
                .hasMessage(ErrorCode.EXISTS_PHONE_NUMBER.getMessage());
    }

    @Test
    @DisplayName("로그인에 성공한다.")
    void login() {
        // given
        // when
        final TokenResponse tokenResponse = memberService.login(loginRequest);

        // then
        assertThat(tokenResponse.grantType()).isEqualTo(GrantType.BEARER.getName());
        assertThat(tokenResponse).hasFieldOrProperty("token");
    }

    @Test
    @DisplayName("존재하지 않는 이메일은 로그인에 실패한다.")
    void loginNotFoundEmail() {
        // given
        final MemberLoginRequest request = new MemberLoginRequest(NEW_EMAIL, NEW_PASSWORD);

        // expect
        assertThatThrownBy(() -> memberService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("잘못된 비밀번호는 로그인에 실패한다.")
    void loginInvalidPassword() {
        // given
        final MemberLoginRequest request = new MemberLoginRequest(USER_EMAIL, NEW_PASSWORD);

        // expect
        assertThatThrownBy(() -> memberService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }
}
