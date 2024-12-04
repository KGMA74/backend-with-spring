package com.esioverflow.api.config;

import java.security.Key;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "w6lY+OkMB0OQBqwJmi//Hz83CSssff6IiuqN2RE/2GYgqXOjkB46tvoBr0qPYQgJ9QkSi5Z0ZZx9yyOz4HWmctedfVu41kQ+ZuyXbcNNJ4ZahBc6mshIOGvx4EYemfTj5naVMu6EzKaf4r4Nz906FillX2PUx24VhaiDn6rxybxi"; //TODO: genrate key on the site allkeysgenerator.com/random/securityemcreptionkeygenrator.espx

    public String extractUsername(String token) {
      return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsresolver){
        final Claims claims = extractAlClaims(token);
        return claimsresolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
        Map<String, Object> extractClaims,
        UserDetails userDetails
    ){
        return Jwts
                .builder()
                .claims(extractClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24 )) // 24 h
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && ! isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extratExpiration(token).before(new Date());
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
