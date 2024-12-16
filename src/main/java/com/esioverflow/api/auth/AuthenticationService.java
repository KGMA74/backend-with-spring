package com.esioverflow.api.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.esioverflow.api.config.JwtService;
import com.esioverflow.api.token.Token;
import com.esioverflow.api.token.TokenRepository;
import com.esioverflow.api.user.User;
import com.esioverflow.api.user.UserRepository;
import com.esioverflow.api.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final AuthCookiesService authCookiesService;

    @Value("${application.security.access-token.name}")
    private String accessCookieName;

    @Value("${application.security.access-token.expriration}")
    private int accessExpiration;

    @Value("${application.security.refresh-token.name}")
    private String refreshCookieName;


    public AuthenticationResponse register(RegisterRequest request, HttpServletResponse response) {
        User user = User.builder()
                        .nickname(request.getNickname())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole())
                        .build();


        userService.saveUser(user);

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        
        saveUserToken(user, accessToken);
        saveUserToken(user, refreshToken) ;

        authCookiesService.setAuthCookies(user, accessToken, refreshToken, response);
        
        return AuthenticationResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
                                    .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), 
                request.getPassword()
            )
        );

        User user = userRepository.findByEmail(request.getEmail())
                            .orElseThrow();

        // here we're gonna make the previous token unusable
        revokeAllUserTokens(user);

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        
        saveUserToken(user, accessToken);
        saveUserToken(user, refreshToken) ;

        authCookiesService.setAuthCookies(user, accessToken, refreshToken, response);
        
        return AuthenticationResponse.builder()
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .build();

    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        var refreshCookie = authCookiesService.getCookie(request, refreshCookieName);

        authCookiesService.deleteCookie(response, accessCookieName);
        
        if(refreshCookie != null){
            // var refreshToken = tokenRepository.findByToken(refrehCookie.getValue());
            String username = jwtService.extractUsername(refreshCookie.getValue());
            User userDetails = userRepository.findByEmail(username)
                                         .orElseThrow();

            if(jwtService.isTokenValid(refreshCookie.getValue(), userDetails)){

                tokenRepository.findAllValidTokensByUser(userDetails.getId()).forEach(token -> {
                    if(!token.getToken().equals(refreshCookie.getValue())){
                        token.ban();
                        tokenRepository.save(token);
                    }
                });
                
                String newAccesstoken = jwtService.generateAccessToken(userDetails);
                authCookiesService.setCookie(response, accessCookieName, newAccesstoken, accessExpiration);
            }

        }
    }

    private void saveUserToken(User user, String token){ 
        // create the token object from the string jwt token and associate it to the given user
        tokenRepository.save(
            Token.builder()
                .token(token)
                .user(user)
                .revoked(false)
                .expired(false)
                .build()
        );
    }

    private void revokeAllUserTokens(User user){
        var storedTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        storedTokens.forEach(token -> token.ban());

        tokenRepository.saveAll(storedTokens);

    }

}
