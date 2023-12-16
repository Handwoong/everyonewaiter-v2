package com.handwoong.everyonewaiter.domain.member.controller;

import static com.handwoong.everyonewaiter.util.Constants.NEW_EMAIL;
import static com.handwoong.everyonewaiter.util.Constants.NEW_PASSWORD;
import static com.handwoong.everyonewaiter.util.Constants.NEW_PHONE_NUMBER;
import static com.handwoong.everyonewaiter.util.Constants.USER_EMAIL;
import static com.handwoong.everyonewaiter.util.Constants.USER_PASSWORD;
import static com.handwoong.everyonewaiter.util.RestDocsUtils.getSpecification;
import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.ControllerTestProvider;
import com.handwoong.everyonewaiter.config.security.jwt.GrantType;
import com.handwoong.everyonewaiter.config.security.jwt.TokenResponse;
import com.handwoong.everyonewaiter.domain.member.dto.MemberLoginRequest;
import com.handwoong.everyonewaiter.domain.member.dto.MemberRegisterRequest;
import com.handwoong.everyonewaiter.exception.ErrorCode;
import com.handwoong.everyonewaiter.exception.ErrorResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MemberControllerTest extends ControllerTestProvider {
    @Test
    @DisplayName("회원가입에 성공한다.")
    void register_member() {
        // given
        final MemberRegisterRequest registerRequest =
                new MemberRegisterRequest(NEW_EMAIL, NEW_PASSWORD, NEW_PHONE_NUMBER);

        // when
        final ExtractableResponse<Response> registerResponse = register(registerRequest);

        // then
        assertThat(registerResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("중복된 이메일이 존재하면 회원가입에 실패한다.")
    void register_duplicate_email() {
        // given
        final MemberRegisterRequest registerRequest =
                new MemberRegisterRequest(USER_EMAIL, USER_PASSWORD, NEW_PHONE_NUMBER);

        // when
        final ExtractableResponse<Response> response = register(registerRequest);
        final ErrorResponse errorResponse = response.body().as(ErrorResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(ErrorCode.EXISTS_EMAIL.statusCode());
        assertThat(errorResponse.message()).isEqualTo(ErrorCode.EXISTS_EMAIL.getMessage());
    }

    @Test
    @DisplayName("중복된 휴대폰 번호가 존재하면 회원가입에 실패한다.")
    void register_duplicate_phone_number() {
        // given
        final MemberRegisterRequest registerRequest = new MemberRegisterRequest(NEW_EMAIL, NEW_PASSWORD, "01011111111");

        final ExtractableResponse<Response> response = register(registerRequest);
        final ErrorResponse errorResponse = response.body().as(ErrorResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(ErrorCode.EXISTS_PHONE_NUMBER.statusCode());
        assertThat(errorResponse.message()).isEqualTo(ErrorCode.EXISTS_PHONE_NUMBER.getMessage());
    }

    @Test
    @DisplayName("로그인에 성공한다.")
    void login_member() {
        // given
        final MemberLoginRequest loginRequest = new MemberLoginRequest(USER_EMAIL, USER_PASSWORD);

        // when
        final ExtractableResponse<Response> response = login(loginRequest);
        final TokenResponse tokenResponse = response.body().as(TokenResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(tokenResponse.grantType()).isEqualTo(GrantType.BEARER.getName());
        assertThat(tokenResponse).hasFieldOrProperty("token");
    }

    @Test
    @DisplayName("존재하지 않는 이메일은 로그인에 실패한다.")
    void login_not_found_email() {
        // given
        final MemberLoginRequest loginRequest = new MemberLoginRequest(NEW_EMAIL, NEW_PASSWORD);

        // when
        final ExtractableResponse<Response> response = login(loginRequest);
        final ErrorResponse errorResponse = response.body().as(ErrorResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(ErrorCode.CHECK_EMAIL_AND_PASSWORD.statusCode());
        assertThat(errorResponse.message()).isEqualTo(ErrorCode.CHECK_EMAIL_AND_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("잘못된 비밀번호는 로그인에 실패한다.")
    void login_invalid_password() {
        // given
        final MemberLoginRequest loginRequest = new MemberLoginRequest(USER_EMAIL, NEW_PASSWORD);

        // when
        final ExtractableResponse<Response> response = login(loginRequest);
        final ErrorResponse errorResponse = response.body().as(ErrorResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(ErrorCode.CHECK_EMAIL_AND_PASSWORD.statusCode());
        assertThat(errorResponse.message()).isEqualTo(ErrorCode.CHECK_EMAIL_AND_PASSWORD.getMessage());
    }

    private ExtractableResponse<Response> register(final MemberRegisterRequest registerRequest) {
        return RestAssured
                .given(getSpecification()).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerRequest)
                .when().post("/api/members")
                .then().log().all().extract();
    }
}
