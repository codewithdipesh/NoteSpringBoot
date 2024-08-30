package com.cosmicdipesh.Note.service;

import com.cosmicdipesh.Note.entity.User;
import com.cosmicdipesh.Note.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final UserRepository userRepository;

    @Autowired
    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public String extractUsername(String token) {
        return extractclaims(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails userdetails){
        String username = extractUsername(token);
        //check the jwt token
        Optional<User> user = userRepository.findByUsername(username); //user will be not null bcz its already checked
        return (username.equals(userdetails.getUsername()))
                && (token.equals(user.get().getAccessToken()))
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractclaims(token,Claims::getExpiration).before(new Date());
    }

    public <T> T extractclaims(String token, Function<Claims,T> resolver){

        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 30L *24*60*60*1000))
                .signWith(getSigninKey())
                .compact();
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
