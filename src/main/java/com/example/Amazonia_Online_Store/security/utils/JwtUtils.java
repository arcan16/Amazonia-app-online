package com.example.Amazonia_Online_Store.security.utils;

import com.example.Amazonia_Online_Store.models.UserEntity;
import com.example.Amazonia_Online_Store.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration.time}")
    private String expirationTime;

    @Autowired
    private UserRepository userRepository;

    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(expirationTime)))
                .signWith(getSignature())
                .compact();
    }

    public SecretKey getSignature(){
        byte [] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isValidToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSignature())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    public String getUserFromToken(String token){
        return getClaims(token, Claims::getSubject);
    }

    public <T> T getClaims(String token, Function<Claims, T>claimsTFunction){
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public Claims extractAllClaims(String token){
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(getSignature())
                .build().parseSignedClaims(token);

        return claimsJws.getPayload();
    }

    public UserEntity getUserFromRequest(HttpServletRequest request) {
        String tokenHeader = request.getHeader("Authorization");

        if(tokenHeader != null){
            String token = tokenHeader.substring(7);
            String username = getUserFromToken(token);
            return userRepository.findByUsername(username)
                    .orElseThrow(()-> new UsernameNotFoundException("El usuario no fue encontrado"));
        }
        return null;
    }
}