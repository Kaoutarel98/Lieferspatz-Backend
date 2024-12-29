package com.uni.lieferspatz.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.uni.lieferspatz.domain.Kunde;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.domain.User;
import com.uni.lieferspatz.service.auth.JwtTokenProvider;
import com.uni.lieferspatz.service.exception.ResourceNotFoundException;

@Service
public class UserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final KundeService kundeService;
    private final RestaurantService restaurantService;

    public UserService(JwtTokenProvider jwtTokenProvider,
            KundeService kundeService,
            RestaurantService restaurantService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.kundeService = kundeService;
        this.restaurantService = restaurantService;
    }

    public String generateJwtToken(Authentication authentication) {
        return this.jwtTokenProvider.generateJwtToken(authentication);
    }

    public User getUserAccount() {
        Optional<Kunde> kunde = kundeService.getCurrentAccount();
        if (kunde.isPresent()) {
            return kunde.get();
        }
        Optional<Restaurant> restaurant = restaurantService.getCurrentAccount();
        if (restaurant.isPresent()) {
            return restaurant.get();
        }
        throw new ResourceNotFoundException("Kein Benutzerkonto gefunden");
    }
}
