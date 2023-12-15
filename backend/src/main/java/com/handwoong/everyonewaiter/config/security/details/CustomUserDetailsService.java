package com.handwoong.everyonewaiter.config.security.details;

import com.handwoong.everyonewaiter.domain.member.model.Member;
import com.handwoong.everyonewaiter.domain.member.repository.MemberRepository;
import com.handwoong.everyonewaiter.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Member member = memberRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(formatExceptionMessage(username)));
        return new CustomUserDetails(member);
    }

    private String formatExceptionMessage(final String username) {
        return String.format("%s - %s", ErrorCode.NOT_FOUND_MEMBER, username);
    }
}
