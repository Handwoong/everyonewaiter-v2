package com.handwoong.everyonewaiter.domain.member.model;

import com.handwoong.everyonewaiter.domain.BaseEntity;
import com.handwoong.everyonewaiter.domain.member.dto.MemberRegisterRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "phone_number"})
})
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

    private Member(final Email email, final Password password, final PhoneNumber phoneNumber) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = MemberRole.ROLE_USER;
        this.status = MemberStatus.INACTIVE;
    }

    public static Member create(final MemberRegisterRequest request, final PasswordEncoder passwordEncoder) {
        final Email email = new Email(request.email());
        final Password password = new Password(request.password()).encode(passwordEncoder);
        final PhoneNumber phoneNumber = new PhoneNumber(request.phoneNumber());
        return new Member(email, password, phoneNumber);
    }

    public boolean compareMismatch(final MemberStatus status) {
        return this.status != status;
    }

    public void changeStatus(final MemberStatus status) {
        this.status = status;
    }

    public String getEmailValue() {
        return getEmail().email();
    }

    public String getPasswordValue() {
        return getPassword().password();
    }
}
