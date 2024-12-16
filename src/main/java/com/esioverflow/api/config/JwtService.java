package com.esioverflow.api.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.esioverflow.api.token.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final TokenRepository tokenRepository;

    @Value("${application.security.secret-key}")
    private String SECRET_KEY;

    @Value("${application.security.refresh-token.expiration}")
    private long refreshExpiration;

    @Value("${application.security.access-token.expriration}")
    private long accessExpiration;

    public String extractUsername(String token) {
      return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsresolver){
        final Claims claims = extractAlClaims(token);
        return claimsresolver.apply(claims);
    }

    public String generateRefreshToken(UserDetails userDetails){
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    public String generateAccessToken(UserDetails userDetails){
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    public String generateAccessToken(
        Map<String, Object> extractClaims,
        UserDetails userDetails
    ){
        return buildToken(extractClaims, userDetails, accessExpiration);
    }

    private String buildToken(
        Map<String, Object> extractClaims,
        UserDetails userDetails,
        long expiration // in second
    ){
        return Jwts
            .builder()
            .claims(extractClaims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000 * expiration))
            .signWith(getSignInKey())
            .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername()) && ! isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extratExpiration(token).before(new Date()) && tokenRepository.findByToken(token)
                                                                            .map(t -> t.isValid())
                                                                            .orElse(false);
    }

    private Date extratExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAlClaims(String token){
        return Jwts
                .parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    
    // Récupérer la clé secrète pour la signature
    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
}
