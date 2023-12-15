package com.handwoong.everyonewaiter.domain.member.service;

import com.handwoong.everyonewaiter.domain.member.dto.MemberRegisterRequest;
import com.handwoong.everyonewaiter.domain.member.model.Email;
import com.handwoong.everyonewaiter.domain.member.model.Member;
import com.handwoong.everyonewaiter.domain.member.model.MemberStatus;
import com.handwoong.everyonewaiter.domain.member.model.PhoneNumber;
import com.handwoong.everyonewaiter.domain.member.repository.MemberRepository;
import com.handwoong.everyonewaiter.exception.ErrorCode;
import com.handwoong.everyonewaiter.exception.EveryoneWaiterException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(final MemberRegisterRequest request) {
        validateDuplicateEmail(request.email());
        validateDuplicatePhoneNumber(request.phoneNumber());

        final Member member = Member.create(request, passwordEncoder);
        member.changeStatus(MemberStatus.ACTIVE);
        memberRepository.save(member);
    }

    private void validateDuplicateEmail(final String email) {
        final boolean isExistsByEmail = memberRepository.existsByEmail(new Email(email));
        if (isExistsByEmail) {
            throw new EveryoneWaiterException(ErrorCode.EXISTS_EMAIL, email);
        }
    }

    private void validateDuplicatePhoneNumber(final String phoneNumber) {
        final boolean isExistsByPhoneNumber = memberRepository.existsByPhoneNumber(new PhoneNumber(phoneNumber));
        if (isExistsByPhoneNumber) {
            throw new EveryoneWaiterException(ErrorCode.EXISTS_PHONE_NUMBER, phoneNumber);
        }
    }
}
