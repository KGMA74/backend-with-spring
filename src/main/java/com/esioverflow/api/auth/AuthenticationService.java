package com.esioverflow.api.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.esioverflow.api.config.JwtService;
import com.esioverflow.api.profile.Profile;
import com.esioverflow.api.token.Token;
import com.esioverflow.api.token.TokenRepository;
import com.esioverflow.api.user.Role;
import com.esioverflow.api.user.User;
import com.esioverflow.api.user.UserRepository;
import com.esioverflow.api.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final TokenRepository tokenRepository;


    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                        .nickname(request.getNickname())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(Role.USER)
                        .build();


        userService.saveUser(user); // this will also create a profile and associate it to our new user
        // or like below
        // user.setProfile(
        //     profileRepository.save(new Profile())
        // );
        // repository.save(user);

        var jwtToken =  jwtService.generateToken(user);

        saveUserToken(user, jwtToken);
        
        return AuthenticationResponse.builder()
                                    .token(jwtToken)
                                    .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), 
                request.getPassword()
            )
        );

        User user = repository.findByEmail(request.getEmail())
                            .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        
        saveUserToken(user, jwtToken); 
        
        return AuthenticationResponse.builder()
                                    .token(jwtToken)
                                    .build();

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

}
