package com.uni.lieferspatz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.lieferspatz.domain.Kunde;
import com.uni.lieferspatz.payload.KundePayload;
import com.uni.lieferspatz.payload.LoginPayload;
import com.uni.lieferspatz.repository.KundeRepository;

//localhost:8080/api/v1/kunde/
@RestController
@RequestMapping(value = "/api/v1/kunde")
public class KundeController {

    private final PasswordEncoder passwordEncoder;
    private final KundeRepository kundeRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public KundeController(PasswordEncoder a, KundeRepository b,
            AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.kundeRepository = b;
        this.passwordEncoder = a;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/erstellen")
    public ResponseEntity<Kunde> kundeErstellen(@RequestBody KundePayload kundePayload) {
        Kunde newKunde = new Kunde();

        newKunde.setVorname(kundePayload.getVorname());
        newKunde.setNachname(kundePayload.getNachname());
        newKunde.setEmail(kundePayload.getEmail().toLowerCase());
        newKunde.setStrasse(kundePayload.getStrasse());
        newKunde.setPlz(kundePayload.getPlz());
        newKunde.setOrt(kundePayload.getOrt());
        String encodedPassword = this.passwordEncoder.encode(kundePayload.getPassword());
        newKunde.setPassword(encodedPassword);

        this.kundeRepository.save(newKunde);

        return new ResponseEntity<>(newKunde, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginPayload loginPayload) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginPayload.getEmail(), loginPayload.getPassword());
        this.authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return new ResponseEntity<>("login war erfolgreich!", HttpStatus.OK);
    }
}
