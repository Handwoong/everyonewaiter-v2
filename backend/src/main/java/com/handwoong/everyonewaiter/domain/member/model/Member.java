package com.handwoong.everyonewaiter.domain.member.model;

import com.handwoong.everyonewaiter.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Embedded
    private Email email;

    @NotNull
    @Embedded
    private Password password;

    @NotNull
    @Embedded
    private PhoneNumber phoneNumber;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    public boolean compareMismatch(final MemberStatus status) {
        return this.status != status;
    }

    public String getEmailValue() {
        return getEmail().email();
    }

    public String getPasswordValue() {
        return getPassword().password();
    }
}
