package com.example.demo.auth.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private String secret;
    private Long expiration;
    private RefreshToken refreshToken = new RefreshToken();
    private String tokenPrefix;
    private String headerString;

    @Getter
    @Setter
    public static class RefreshToken {
        private Long expiration;
    }
}