package com.handwoong.everyonewaiter.config.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GrantType {
    BEARER("Bearer"),
    ;

    private final String name;
}
