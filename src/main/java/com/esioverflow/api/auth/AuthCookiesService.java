package com.esioverflow.api.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.esioverflow.api.config.JwtService;
import com.esioverflow.api.token.TokenRepository;
import com.esioverflow.api.user.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
public class AuthCookiesService {
    @Value("${application.security.access-token.name}")
    private String accessCookieName;

    @Value("${application.security.access-token.expriration}")
    private int accessExpiration;

    @Value("${application.security.refresh-token.name}")
    private String refreshCookieName;

    @Value("${application.security.refresh-token.expiration}")
    private int refreshExpiration;

    @Value("${application.security.HttpOnly}")
    private boolean HttpOnly;

    @Value("${application.security.Secure}")
    private boolean secure;

    private String domain = "localhost"; // il faudra le depalacer dans application.yml

    public Cookie getCookie(HttpServletRequest request, String name){
        var cookies = request.getCookies();

        if(cookies != null)
        for (Cookie cookie : cookies) {
            if(cookie.getName().matches(name))
                return cookie;
        }
        return null;
    }

    public void setAuthCookies(User user, String accessToken, String refreshToken, HttpServletResponse response){ // gerates access and refresh token and set in cookies
        setCookie(response, accessCookieName, accessToken, accessExpiration);
        setCookie(response, refreshCookieName, refreshToken, refreshExpiration);
    }

    public void deleteCookie(HttpServletResponse response, String name){
        var cookie = new Cookie(name, accessCookieName);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public void setCookie(HttpServletResponse response, String name, String value, int maxAge){
        setCookie(response, name, value, "/", maxAge, domain, HttpOnly, secure);
    }

    public void setCookie(HttpServletResponse response, String name, String value, String path, int maxAge, String domain, boolean HttpOnly, boolean secure){
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(HttpOnly);
        cookie.setPath(path);
        cookie.setSecure(secure);

        response.addCookie(cookie);
    }
}
