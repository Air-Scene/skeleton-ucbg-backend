package com.example.demo.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    @Value("${app.jwt.refresh-token.cookie-name:refreshToken}")
    private String refreshTokenCookieName;

    @Value("${app.cookie.domain:localhost}")
    private String cookieDomain;

    public void addRefreshTokenToCookies(HttpServletResponse response, String refreshToken) {
        Cookie cookie = createCookie(refreshToken, 7 * 24 * 60 * 60);

        response.addHeader(
            "Set-Cookie", 
            cookie.getName() + "=" + cookie.getValue() + "; " +
            "Max-Age=" + cookie.getMaxAge() + "; " +
            "Path=" + cookie.getPath() + "; " +
            "Domain=" + cookie.getDomain() + "; " +
            "HttpOnly; " +
            "Secure; " +
            "SameSite=Lax"
        );
    }

    public void deleteRefreshTokenToCookies(HttpServletResponse response) {
        Cookie cookie = createCookie("", 0);

        response.addHeader(
            "Set-Cookie", 
            cookie.getName() + "=; " +
            "Max-Age=" + cookie.getMaxAge() + "; " +
            "Path=" + cookie.getPath() + "; " +
            "Domain=" + cookie.getDomain() + "; " +
            "HttpOnly; " +
            "Secure; " +
            "SameSite=Lax"
        );
    }

    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (refreshTokenCookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
    
        return null;
    }

    private Cookie createCookie(String refreshTokenValue, int maxAge) {
        Cookie cookie = new Cookie(refreshTokenCookieName, refreshTokenValue);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setDomain(cookieDomain);
        
        return cookie;
    }
} 