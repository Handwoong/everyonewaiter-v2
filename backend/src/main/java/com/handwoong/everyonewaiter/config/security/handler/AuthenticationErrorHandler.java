package com.handwoong.everyonewaiter.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.handwoong.everyonewaiter.exception.ErrorCode;
import com.handwoong.everyonewaiter.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationErrorHandler {
    private final ErrorCode errorCode;
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    protected void handle(final HttpServletResponse response) throws IOException {
        final ErrorResponse errorResponse = ErrorResponse.from(errorCode);
        final String body = mapper.writeValueAsString(errorResponse);
        response.setStatus(errorCode.statusCode());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(body);
    }
}
