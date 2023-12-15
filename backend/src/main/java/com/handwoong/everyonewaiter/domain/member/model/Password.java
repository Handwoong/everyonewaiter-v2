package com.handwoong.everyonewaiter.domain.member.model;

import com.handwoong.everyonewaiter.exception.ErrorCode;
import com.handwoong.everyonewaiter.exception.EveryoneWaiterException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

@Embeddable
public record Password(
        @Column(nullable = false)
        String password
) {
    public static final Pattern PASSWORD_REGEX_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,}$");

    public Password {
        validateNull(password);
        validateFormat(password);
    }

    private void validateNull(final String password) {
        if (!StringUtils.hasText(password)) {
            throw new EveryoneWaiterException(ErrorCode.INVALID_EMAIL, password);
        }
    }

    private void validateFormat(final String password) {
        if (!PASSWORD_REGEX_PATTERN.matcher(password).matches()) {
            throw new EveryoneWaiterException(ErrorCode.INVALID_EMAIL, password);
        }
    }
}
