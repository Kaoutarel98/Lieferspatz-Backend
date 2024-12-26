package com.uni.lieferspatz.service.auth;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private final JwtParser tokenParser;
    private final Key key;

    public JwtTokenProvider() {
        // this.cgtProperties = cgtProperties;
        String secret = this.getBase64Secret();
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public String generateJwtToken(Authentication authentication) {
        String authorities = authentication.getAuthorities()//
                .stream()//
                .map(GrantedAuthority::getAuthority)//
                .collect(Collectors.joining(","));
        Date expirationDate = this.getExpirationDate();
        JwtBuilder jwtBuilder = Jwts.builder()//
                .setSubject(authentication.getName())//
                .signWith(this.key, SignatureAlgorithm.HS512)//
                .setExpiration(expirationDate);
        jwtBuilder.claim(AUTHORITIES_KEY, authorities);
        return jwtBuilder.compact();
    }

    private Date getExpirationDate() {
        long currentTime = new Date().getTime();
        long validity = 86400 * 1000; // 24h in ms
        return new Date(currentTime + validity);
    }

    public Optional<Authentication> extractAuthentication(String token) {
        try {
            // Parse the token and extract claims
            return Optional.ofNullable(token)
                    .map(this.tokenParser::parseClaimsJws)
                    .map(Jws::getBody)
                    .map(body -> this.createAuthenticationToken(body, token))
                    .or(() -> {
                        log.error("Failed to extract authentication: Claims are null or empty.");
                        return Optional.empty();
                    });
        } catch (Exception e) {
            log.error("Error while extracting authentication from token: {}", token, e);
            return Optional.empty();
        }
    }

    private Authentication createAuthenticationToken(Claims claims, String token) {
        // Extract authorities
        Collection<? extends GrantedAuthority> authorities = Arrays//
                .stream(claims.getOrDefault(AUTHORITIES_KEY, "").toString()//
                        .split(","))//
                .filter(StringUtils::hasText)//
                .map(SimpleGrantedAuthority::new)//
                .collect(Collectors.toList());

        String subject = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(subject, token, authorities);
    }

    public String getBase64Secret() {
        return "XExb6dW5DZWUkdyt0BaDuA6biZ3UmEnojmq11zz+ko88s8KXpf2CHqoWbc4oW5RRUgL3lIH437pJWVM7nBTAPgXHnDSyLQBuhs8/RZHmRViuguQ149/TpXmyw5M4A2ooVTcNpsfP5GsfGx30C7yXaPQijXpyr+oEupS5rEEzAMsEc148Ji1Ec8EK6R+BiJr1Rq/r+hvsGroEibW/gmTsAZu36XecgAqDa2sUCK5hFSWfLIor+y9Qhx0lj+f9FEVCM48LfpqwUmnXZk9BZkhOXPWRaEq3TUkFaymWKEnGCDx41TbZ/mu9Z65Od/K2GpXKKqdul/2NmNDpdWD9kyDq7JYyhdAM6R/LRVw05Tgxd3WgIxMPWRABrE5UWYQsDWXc2LZd1QNRDhTOOIFmCZPFrn4DVbADayVvX/zNPxV7ks5WnEs5uZStamo+ebjLS8hKrdbrFUEItgRSCEwDqz+3IAnENESk/88QxTLMJNQKJv2+fdd6oApYQSiV61CVH0V5099QD/0vZytNvUE9u0d4BAHNLQFTURKuqYOw1s4DnrQLBnlQ1ldOXvFVRj0HZ6VOhnbK5uG4AmwctRT2YwPn1FTFDWB2W/2Oy4tZf6/HStfyAFKxTURQDYFHa7AtkDjXs0FSKxgMyjCEpQdtrCs/xuVmqCE8d2agF4/JWr7b0hI=";
    }
}
