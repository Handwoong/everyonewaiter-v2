package com.handwoong.everyonewaiter.config.security.uri;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnonymousAllowUri implements AllowUri {
    MEMBER_REGISTER("/api/members"),
    MEMBER_LOGIN("/api/members/login"),
    ;

    private final String uri;
}
