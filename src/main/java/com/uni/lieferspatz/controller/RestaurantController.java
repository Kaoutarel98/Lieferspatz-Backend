package com.uni.lieferspatz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.payload.RestaurantPayload;
import com.uni.lieferspatz.repository.RestaurantRepository;
import com.uni.lieferspatz.service.mapper.UserMapper;

@RestController
@RequestMapping(value = "/api/v1/restaurant")
public class RestaurantController {

    private final PasswordEncoder passwordEncoder;
    private final RestaurantRepository restaurantRepository;

    public RestaurantController(PasswordEncoder passwordEncoder,
            RestaurantRepository restaurantRepository) {
        this.passwordEncoder = passwordEncoder;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping("/erstellen")
    public ResponseEntity<Restaurant> restaurantErstellen(@RequestBody RestaurantPayload restaurantPayload) {
        Restaurant newRestaurant = UserMapper.mapRestaurant(restaurantPayload, passwordEncoder);

        this.restaurantRepository.save(newRestaurant);

        return new ResponseEntity<>(newRestaurant, HttpStatus.OK);
    }
}
