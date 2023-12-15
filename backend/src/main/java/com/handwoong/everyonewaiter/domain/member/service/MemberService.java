package com.handwoong.everyonewaiter.domain.member.service;

import com.handwoong.everyonewaiter.domain.member.dto.MemberRegisterRequest;

public interface MemberService {
    void register(MemberRegisterRequest request);
}
