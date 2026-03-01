package com.digiSchool.digiSchool.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Utilitaire pour la gestion des cookies HttpOnly JWT.
 *
 * Utilise addHeader("Set-Cookie", ...) pour inclure SameSite=Strict,
 * car l'API Servlet Jakarta ne supporte pas SameSite nativement avant Servlet 6.
 */
public class CookieUtils {

    public static final String ACCESS_TOKEN_COOKIE = "access_token";
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    public static void addAccessTokenCookie(HttpServletResponse response, String token,
                                            int maxAgeSeconds, boolean secure) {
        response.addHeader("Set-Cookie", buildCookieHeader(ACCESS_TOKEN_COOKIE, token, maxAgeSeconds, secure));
    }

    public static void addRefreshTokenCookie(HttpServletResponse response, String token,
                                             int maxAgeSeconds, boolean secure) {
        response.addHeader("Set-Cookie", buildCookieHeader(REFRESH_TOKEN_COOKIE, token, maxAgeSeconds, secure));
    }

    public static void clearTokenCookies(HttpServletResponse response, boolean secure) {
        response.addHeader("Set-Cookie", buildCookieHeader(ACCESS_TOKEN_COOKIE, "", 0, secure));
        response.addHeader("Set-Cookie", buildCookieHeader(REFRESH_TOKEN_COOKIE, "", 0, secure));
    }

    public static String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                String value = cookie.getValue();
                return (value != null && !value.isEmpty()) ? value : null;
            }
        }
        return null;
    }

    private static String buildCookieHeader(String name, String value, int maxAgeSeconds, boolean secure) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("=").append(value);
        sb.append("; Max-Age=").append(maxAgeSeconds);
        sb.append("; Path=/");
        sb.append("; HttpOnly");
        sb.append("; SameSite=Strict");
        if (secure) {
            sb.append("; Secure");
        }
        return sb.toString();
    }
}
