package com.uni.lieferspatz.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.lieferspatz.domain.Item;
import com.uni.lieferspatz.domain.Kunde;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.dto.api.AvailableRestaurantApi;
import com.uni.lieferspatz.dto.api.ItemApi;
import com.uni.lieferspatz.dto.payload.KundePayload;
import com.uni.lieferspatz.service.KundeService;
import com.uni.lieferspatz.service.mapper.ItemMapper;
import com.uni.lieferspatz.service.mapper.RestaurantMapper;

@RestController
@RequestMapping(value = "/api/v1/kunde")
public class KundeController {

    private final KundeService kundeService;

    public KundeController(KundeService kundeService) {
        this.kundeService = kundeService;
    }

    @PostMapping("/erstellen")
    public ResponseEntity<Kunde> kundeErstellen(@RequestBody KundePayload kundePayload) {
        this.kundeService.saveNeueKunde(kundePayload);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/restaurant/get")
    public ResponseEntity<List<AvailableRestaurantApi>> getRelatedRestaurant() {
        List<Restaurant> restaurants = this.kundeService.getRelatedRestaurant();
        List<AvailableRestaurantApi> result = RestaurantMapper.mapToAvailableRestaurantApi(restaurants);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}/items")
    public ResponseEntity<List<ItemApi>> getRestaurantItems(@PathVariable Long restaurantId) {
        List<Item> items = this.kundeService.getRestaurantItems(restaurantId);
        List<ItemApi> result = ItemMapper.mapToItemApi(items);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
