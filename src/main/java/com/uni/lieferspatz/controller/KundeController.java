package com.uni.lieferspatz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.lieferspatz.domain.Kunde;
import com.uni.lieferspatz.dto.payload.KundePayload;
import com.uni.lieferspatz.repository.KundeRepository;
import com.uni.lieferspatz.repository.RestaurantRepository;
import com.uni.lieferspatz.service.mapper.UserMapper;

@RestController
@RequestMapping(value = "/api/v1/kunde")
public class KundeController {

    private final PasswordEncoder passwordEncoder;
    private final KundeRepository kundeRepository;

    public KundeController(PasswordEncoder passwordEncoder, KundeRepository kundeRepository,
            RestaurantRepository restaurantRepository) {
        this.kundeRepository = kundeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/erstellen")
    public ResponseEntity<Kunde> kundeErstellen(@RequestBody KundePayload kundePayload) {
        Kunde newKunde = UserMapper.mapKunde(kundePayload, passwordEncoder);

        this.kundeRepository.save(newKunde);

        return new ResponseEntity<>(newKunde, HttpStatus.OK);
    }

}
