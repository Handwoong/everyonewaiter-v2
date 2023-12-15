package com.handwoong.everyonewaiter.domain.member.model;

import com.handwoong.everyonewaiter.exception.ErrorCode;
import com.handwoong.everyonewaiter.exception.EveryoneWaiterException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Embeddable
public record Password(
        @Column(nullable = false)
        String password
) {
    public static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    public Password {
        validateNull(password);
        validateFormat(password);
    }

    private void validateNull(final String password) {
        if (!StringUtils.hasText(password)) {
            throw new EveryoneWaiterException(ErrorCode.INVALID_PASSWORD, password);
        }
    }

    private void validateFormat(final String password) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new EveryoneWaiterException(ErrorCode.INVALID_PASSWORD, password);
        }
    }

    public Password encode(final PasswordEncoder passwordEncoder) {
        final String encodedPassword = passwordEncoder.encode(password);
        return new Password(encodedPassword);
    }
}
