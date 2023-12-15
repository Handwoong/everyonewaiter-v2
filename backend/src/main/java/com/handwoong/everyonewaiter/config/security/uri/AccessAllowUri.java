package com.handwoong.everyonewaiter.config.security.uri;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccessAllowUri implements AllowUri {
    ROOT("/"),
    INDEX("/index.html"),
    ;

    private final String uri;
}
