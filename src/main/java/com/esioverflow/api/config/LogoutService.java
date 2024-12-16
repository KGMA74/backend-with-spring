package com.esioverflow.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.esioverflow.api.auth.AuthCookiesService;
import com.esioverflow.api.token.Token;
import com.esioverflow.api.token.TokenRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final AuthCookiesService authCookiesService;

    @Value("${application.security.access-token.name}")
    private String accessCookieName;

    @Value("${application.security.refresh-token.name}")
    private String refreshCookieName;

    @Override
    public void logout(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) {

        Cookie accessCookie = authCookiesService.getCookie(request, accessCookieName);
        Cookie refreshCookie = authCookiesService.getCookie(request, refreshCookieName);

        if(accessCookie != null){
            Token accessToken = tokenRepository.findByToken(accessCookie.getValue()).orElse(null);

            //TODO pourquoi le accessToken est null
            // reasons: when we set the access and refresh expirations date to same value. 
            // at the a given time when we genrate accessToken and the refreshToken they get the same value
            // consequence when we use the TokenRepository to find the token of which the value is passed in paremetters
            // it return null because it finds Token object in db with the same value(token)
            accessToken.ban();
            tokenRepository.save(accessToken);

            authCookiesService.deleteCookie(response, accessCookieName);
        }

        if(refreshCookie != null){
            Token refreshToken = tokenRepository.findByToken(refreshCookie.getValue()).orElse(null);

            refreshToken.ban();
            tokenRepository.save(refreshToken);

          
            authCookiesService.deleteCookie(response, refreshCookieName);
        }

    }

}