package com.handwoong.everyonewaiter.domain.member.dto;

import static com.handwoong.everyonewaiter.util.ValidationMessage.EMAIL_MESSAGE;
import static com.handwoong.everyonewaiter.util.ValidationMessage.EMPTY_MESSAGE;
import static com.handwoong.everyonewaiter.util.ValidationMessage.PASSWORD_MESSAGE;

import com.handwoong.everyonewaiter.domain.member.model.Email;
import com.handwoong.everyonewaiter.domain.member.model.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberLoginRequest(
        @NotBlank(message = EMPTY_MESSAGE)
        @Pattern(regexp = Email.EMAIL_REGEX, message = EMAIL_MESSAGE)
        String email,

        @NotBlank(message = EMPTY_MESSAGE)
        @Pattern(regexp = Password.PASSWORD_REGEX, message = PASSWORD_MESSAGE)
        String password
) {
}
