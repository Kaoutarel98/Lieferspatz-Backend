package com.uni.lieferspatz.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.lieferspatz.domain.User;
import com.uni.lieferspatz.dto.api.UserApi;
import com.uni.lieferspatz.dto.payload.LoginPayload;
import com.uni.lieferspatz.service.UserService;
import com.uni.lieferspatz.service.auth.JwtFilter;
import com.uni.lieferspatz.service.mapper.UserMapper;

// localhost:8080/api/v1/kunde/
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    public UserController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginPayload loginPayload) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginPayload.getEmail(), loginPayload.getPassword());
        Authentication authentication = this.authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String jwt = this.userService.generateJwtToken(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserApi> getAccount() {
        User user = this.userService.getUserAccount();
        UserApi userApi = UserMapper.mapToUserApi(user);
        if (userApi != null) {
            return new ResponseEntity<>(userApi, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
