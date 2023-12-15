package com.handwoong.everyonewaiter.config.security.handler;

import com.handwoong.everyonewaiter.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint extends AuthenticationErrorHandler implements AuthenticationEntryPoint {
    public CustomAuthenticationEntryPoint() {
        super(ErrorCode.UN_AUTHORIZE);
    }

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        handle(response);
    }
}
