package com.handwoong.everyonewaiter.domain.member.service;

import com.handwoong.everyonewaiter.config.security.jwt.TokenResponse;
import com.handwoong.everyonewaiter.domain.member.dto.MemberLoginRequest;
import com.handwoong.everyonewaiter.domain.member.dto.MemberRegisterRequest;

public interface MemberService {
    void register(MemberRegisterRequest request);

    TokenResponse login(MemberLoginRequest request);
}
