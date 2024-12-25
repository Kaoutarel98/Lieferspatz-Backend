package com.uni.lieferspatz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.payload.ItemPayload;
import com.uni.lieferspatz.payload.RestaurantPayload;
import com.uni.lieferspatz.repository.RestaurantRepository;
import com.uni.lieferspatz.service.RestaurantService;
import com.uni.lieferspatz.service.mapper.UserMapper;

@RestController
@RequestMapping(value = "/api/v1/restaurant")
public class RestaurantController {

    private final PasswordEncoder passwordEncoder;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;

    public RestaurantController(PasswordEncoder passwordEncoder,
            RestaurantRepository restaurantRepository, RestaurantService restaurantService) {
        this.passwordEncoder = passwordEncoder;
        this.restaurantRepository = restaurantRepository;
        this.restaurantService = restaurantService;
    }

    @PostMapping("/erstellen")
    public ResponseEntity<Restaurant> erstellen(@RequestBody RestaurantPayload restaurantPayload) {
        Restaurant newRestaurant = UserMapper.mapRestaurant(restaurantPayload, passwordEncoder);

        this.restaurantRepository.save(newRestaurant);

        return new ResponseEntity<>(newRestaurant, HttpStatus.OK);
    }

    @PostMapping("/{restaurantId}/item/add")
    public ResponseEntity<String> addItem(@PathVariable Long restaurantId, @RequestBody ItemPayload itemPayload) {
        this.restaurantService.saveItem(restaurantId, itemPayload);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/item/delete/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable Long itemId) {
        this.restaurantService.deleteItem(itemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/item/update")
    public ResponseEntity<String> updateItem(@RequestBody ItemPayload itemPayload) {
        this.restaurantService.updateItem(itemPayload);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
