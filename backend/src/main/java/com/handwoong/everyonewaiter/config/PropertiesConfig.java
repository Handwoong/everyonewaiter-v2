package com.handwoong.everyonewaiter.config;

import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PropertiesConfig {
    @Value("#{${client.urls}}")
    private List<String> clientUrls;

    @Value("${jwt.secret}")
    private String jwtSecretKey;
}
