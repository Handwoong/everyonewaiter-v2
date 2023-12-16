package com.handwoong.everyonewaiter.domain.member.model;

import org.springframework.security.crypto.password.PasswordEncoder;

public class FakePasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(final CharSequence rawPassword) {
        return rawPassword + "encode";
    }

    @Override
    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        return encodedPassword.equals(rawPassword + "encode");
    }
}
