package com.handwoong.everyonewaiter.config.security.details;

import com.handwoong.everyonewaiter.domain.member.model.Member;
import com.handwoong.everyonewaiter.domain.member.model.MemberStatus;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
    private final Member member;
    private final List<GrantedAuthority> roles = new ArrayList<>();

    public CustomUserDetails(final Member member) {
        this.member = member;
        roles.add(new SimpleGrantedAuthority(member.getRole().name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return member.getPasswordValue();
    }

    @Override
    public String getUsername() {
        return member.getEmailValue();
    }

    @Override
    public boolean isAccountNonExpired() {
        return member.compareMismatch(MemberStatus.SLEEP);
    }

    @Override
    public boolean isAccountNonLocked() {
        return member.compareMismatch(MemberStatus.LOCK);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return member.compareMismatch(MemberStatus.INACTIVE);
    }

    @Override
    public boolean isEnabled() {
        return member.compareMismatch(MemberStatus.LEAVE);
    }
}
