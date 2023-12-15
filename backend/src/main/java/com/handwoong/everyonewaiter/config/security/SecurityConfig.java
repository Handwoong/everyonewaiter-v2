package com.handwoong.everyonewaiter.config.security;

import com.handwoong.everyonewaiter.config.security.handler.CustomAccessDeniedHandler;
import com.handwoong.everyonewaiter.config.security.handler.CustomAuthenticationEntryPoint;
import com.handwoong.everyonewaiter.config.security.jwt.JwtTokenFilter;
import com.handwoong.everyonewaiter.config.security.uri.AccessAllowUri;
import com.handwoong.everyonewaiter.config.security.uri.AllowUri;
import com.handwoong.everyonewaiter.config.security.uri.AnonymousAllowUri;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        addBasicConfigToHttpSecurity(http);
        addAuthorizeConfigToHttpSecurity(http);
        addExceptionHandlerToHttpSecurity(http);
        addFilterBefore(http);
        return http.build();
    }

    private void addBasicConfigToHttpSecurity(final HttpSecurity http) throws Exception {
        http.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    private void addAuthorizeConfigToHttpSecurity(final HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(convertUriToPathMatcher(AccessAllowUri.values())).permitAll()
                        .requestMatchers(convertUriToPathMatcher(AnonymousAllowUri.values())).anonymous()
                        .anyRequest().authenticated()
        );
    }

    private AntPathRequestMatcher[] convertUriToPathMatcher(final AllowUri[] allowUris) {
        return Arrays.stream(allowUris)
                .map(AllowUri::getUri)
                .map(AntPathRequestMatcher::antMatcher)
                .toArray(AntPathRequestMatcher[]::new);
    }

    private void addExceptionHandlerToHttpSecurity(final HttpSecurity http) throws Exception {
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()));
    }

    private void addFilterBefore(final HttpSecurity http) {
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
