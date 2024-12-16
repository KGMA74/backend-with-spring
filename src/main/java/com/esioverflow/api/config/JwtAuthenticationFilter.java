package com.esioverflow.api.config;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.esioverflow.api.auth.AuthCookiesService;
import com.esioverflow.api.auth.AuthenticationService;
import com.esioverflow.api.token.TokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtService jwtService;
    private final AuthCookiesService authCookiesService;
    private final UserDetailsService userDetailsService;
    
    @Value("${application.security.access-token.name}")
    private String accessCookieName;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        // final String authHeader = request.getHeader("Authorization");
        Cookie accessCookie = authCookiesService.getCookie(request, accessCookieName);
    
        final String jwt;
        final String userEmail;


        
        if(accessCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // if(authHeader == null || !authHeader.startsWith("Bearer ")){
        //     // mean there's no creation and refresh token in the request header
        //     filterChain.doFilter(request, response);
        //     return;
        // }


        jwt = accessCookie.getValue();
        userEmail = jwtService.extractUsername(jwt);
        

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            
            if(jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userEmail,
                    true, 
                    userDetails.getAuthorities()
                );

                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.warn("\n\n--------------------\n\nuserEmail: " + userEmail + "\n\n------------\n");


            }
        }

        filterChain.doFilter(request, response);

    }
    
}