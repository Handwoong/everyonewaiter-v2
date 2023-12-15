package com.handwoong.everyonewaiter.domain.member.model;

import com.handwoong.everyonewaiter.exception.ErrorCode;
import com.handwoong.everyonewaiter.exception.EveryoneWaiterException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

@Embeddable
public record PhoneNumber(
        @Column(length = 20, nullable = false)
        String phoneNumber
) {
    public static final String PHONE_NUMBER_REGEX = "^01[016789]\\d{7,8}$";
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

    public PhoneNumber {
        validateNull(phoneNumber);
        validateFormat(phoneNumber);
    }

    private void validateNull(final String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            throw new EveryoneWaiterException(ErrorCode.INVALID_PHONE_NUMBER, phoneNumber);
        }
    }

    private void validateFormat(final String phoneNumber) {
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            throw new EveryoneWaiterException(ErrorCode.INVALID_PHONE_NUMBER, phoneNumber);
        }
    }
}
