package com.uni.lieferspatz.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.uni.lieferspatz.service.auth.JwtTokenProvider;

@Service
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;

    public UserService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String generateJwtToken(Authentication authentication) {
        return this.jwtTokenProvider.generateJwtToken(authentication);
    }

}
