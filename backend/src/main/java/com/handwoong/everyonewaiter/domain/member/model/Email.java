package com.handwoong.everyonewaiter.domain.member.model;

import com.handwoong.everyonewaiter.exception.ErrorCode;
import com.handwoong.everyonewaiter.exception.EveryoneWaiterException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

@Embeddable
public record Email(
        @Column(length = 50, unique = true, nullable = false)
        String email
) {
    public static final Pattern EMAIL_REGEX_PATTERN =
            Pattern.compile("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");

    public Email {
        validateNull(email);
        validateFormat(email);
    }

    private void validateNull(final String email) {
        if (!StringUtils.hasText(email)) {
            throw new EveryoneWaiterException(ErrorCode.INVALID_EMAIL, email);
        }
    }

    private void validateFormat(final String email) {
        if (!EMAIL_REGEX_PATTERN.matcher(email).matches()) {
            throw new EveryoneWaiterException(ErrorCode.INVALID_EMAIL, email);
        }
    }
}
