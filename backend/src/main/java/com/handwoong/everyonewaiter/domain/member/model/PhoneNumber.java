package com.handwoong.everyonewaiter.domain.member.model;

import com.handwoong.everyonewaiter.exception.ErrorCode;
import com.handwoong.everyonewaiter.exception.EveryoneWaiterException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

@Embeddable
public record PhoneNumber(
        @Column(length = 20, unique = true, nullable = false)
        String phoneNumber
) {
    public static final Pattern PHONE_NUMBER_REGEX_PATTERN = Pattern.compile("^01[016789]\\d{7,8}$");

    public PhoneNumber {
        validateNull(phoneNumber);
        validateFormat(phoneNumber);
    }

    private void validateNull(final String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            throw new EveryoneWaiterException(ErrorCode.INVALID_EMAIL, phoneNumber);
        }
    }

    private void validateFormat(final String phoneNumber) {
        if (!PHONE_NUMBER_REGEX_PATTERN.matcher(phoneNumber).matches()) {
            throw new EveryoneWaiterException(ErrorCode.INVALID_EMAIL, phoneNumber);
        }
    }
}
