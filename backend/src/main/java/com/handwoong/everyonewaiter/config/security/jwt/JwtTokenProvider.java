package com.handwoong.everyonewaiter.config.security.jwt;

import static com.handwoong.everyonewaiter.util.TimeConstants.TEN_MINUTE;

import com.handwoong.everyonewaiter.config.PropertiesConfig;
import com.handwoong.everyonewaiter.exception.ErrorCode;
import com.handwoong.everyonewaiter.exception.EveryoneWaiterException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.crypto.SecretKey;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private static final String AUTH_CLAIM_KEY = "roles";
    private static final String EMAIL = "email";
    private static final String AUTHORITY = "authority";

    private final SecretKey secretKey;

    public JwtTokenProvider(final PropertiesConfig config) {
        final byte[] keyBytes = Decoders.BASE64.decode(config.getJwtSecretKey());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenResponse createToken(final GrantType grantType, final Authentication authentication) {
        final String authority = getAuthority(authentication);
        final String token =
                generateToken(authentication.getName(), authority);
        return TokenResponse.of(grantType, token);
    }

    public TokenResponse createToken(final GrantType grantType,
                                     final String subject,
                                     final String claimKey,
                                     final String claim) {
        final String token = generateToken(subject, claimKey, claim, generateExpireDate(TEN_MINUTE));
        return TokenResponse.of(grantType, token);
    }

    public Date generateExpireDate(final long expireTime) {
        return new Date(System.currentTimeMillis() + expireTime);
    }

    private String getAuthority(final Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findAny()
                .orElseThrow(() -> new EveryoneWaiterException(ErrorCode.UN_AUTHORIZE));
    }

    private String generateToken(final String subject, final String claim) {
        return Jwts.builder()
                .subject(subject)
                .claim(AUTH_CLAIM_KEY, claim)
                .signWith(secretKey)
                .compact();
    }

    private String generateToken(final String subject,
                                 final String claimKey,
                                 final String claim,
                                 final Date expireDate) {
        return Jwts.builder()
                .subject(subject)
                .claim(claimKey, claim)
                .signWith(secretKey)
                .expiration(expireDate)
                .compact();
    }

    public Authentication parseToken(final String token) {
        final Map<String, String> payload = parsePayload(token);
        final List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(payload.get(AUTHORITY)));
        final User principal = new User(payload.get(EMAIL), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public String parseToken(final String token, final String claimKey) {
        final Claims claims = parseClaims(token);
        validateClaimKey(claims, claimKey);
        return claims.getSubject();
    }

    private void validateClaimKey(final Claims claims, final String claimKey) {
        if (!claims.containsKey(claimKey)) {
            throw new EveryoneWaiterException(ErrorCode.INVALID_CLAIM_KEY, claimKey);
        }
    }

    private Map<String, String> parsePayload(final String token) {
        final Claims claims = parseClaims(token);
        final Map<String, String> payload = new HashMap<>();
        payload.put(EMAIL, claims.getSubject());
        payload.put(AUTHORITY, claims.get(AUTH_CLAIM_KEY).toString());
        return payload;
    }

    private Claims parseClaims(final String token) {
        try {
            validateToken(token);
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (final IllegalArgumentException | JwtException exception) {
            throw new EveryoneWaiterException(ErrorCode.INVALID_TOKEN);
        }
    }

    private void validateToken(final String token) {
        if (Objects.isNull(token)) {
            throw new EveryoneWaiterException(ErrorCode.INVALID_TOKEN);
        }
    }
}
