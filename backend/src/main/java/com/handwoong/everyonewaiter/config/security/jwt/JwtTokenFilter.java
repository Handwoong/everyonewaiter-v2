package com.handwoong.everyonewaiter.config.security.jwt;

import com.handwoong.everyonewaiter.exception.EveryoneWaiterException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilter {
    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    public static final String AUTHORIZATION_HEADER_TYPE = "Bearer";

    private final JwtTokenProvider tokenProvider;

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        final String token = resolveToken(servletRequest);
        saveAuthentication(token);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(final ServletRequest servletRequest) {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final String bearerToken = request.getHeader(AUTHORIZATION_HEADER_KEY);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_HEADER_TYPE + " ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void saveAuthentication(final String token) {
        try {
            final Authentication authentication = tokenProvider.parseToken(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (final EveryoneWaiterException exception) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }
}
