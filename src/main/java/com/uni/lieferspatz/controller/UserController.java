package com.uni.lieferspatz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.lieferspatz.payload.LoginPayload;

//localhost:8080/api/v1/kunde/
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserController(
            AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginPayload loginPayload) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginPayload.getEmail(), loginPayload.getPassword());
        this.authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return new ResponseEntity<>("login war erfolgreich!", HttpStatus.OK);
    }
}
