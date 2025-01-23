package com.uni.lieferspatz.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.lieferspatz.domain.Bestellung;
import com.uni.lieferspatz.domain.Item;
import com.uni.lieferspatz.domain.OpeningHours;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.dto.api.BestellungApi;
import com.uni.lieferspatz.dto.api.ItemApi;
import com.uni.lieferspatz.dto.api.OpeningHoursApi;
import com.uni.lieferspatz.dto.payload.ItemPayload;
import com.uni.lieferspatz.dto.payload.LieferPlzPayload;
import com.uni.lieferspatz.dto.payload.OpeningHoursPayload;
import com.uni.lieferspatz.dto.payload.RestaurantPayload;
import com.uni.lieferspatz.service.BestellungService;
import com.uni.lieferspatz.service.RestaurantService;
import com.uni.lieferspatz.service.mapper.BestellungMapper;
import com.uni.lieferspatz.service.mapper.ItemMapper;
import com.uni.lieferspatz.service.mapper.OpeningHoursMapper;
import com.uni.lieferspatz.service.mapper.UserMapper;

@RestController
@RequestMapping(value = "/api/v1/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final BestellungService bestellungService;

    public RestaurantController(RestaurantService restaurantService,
            BestellungService bestellungService) {
        this.restaurantService = restaurantService;
        this.bestellungService = bestellungService;
    }

    @PostMapping("/erstellen")
    public ResponseEntity<Void> erstellen(@RequestBody RestaurantPayload restaurantPayload) {
        Restaurant newRestaurant = UserMapper.mapFromRestaurantPayload(restaurantPayload);
        this.restaurantService.saveNeueRestaurant(newRestaurant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/plz/add")
    public ResponseEntity<String> addPlz(
            @RequestBody LieferPlzPayload plz) {
        this.restaurantService.savePlz(plz);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/opening/add")
    public ResponseEntity<String> addOpeningHours(
            @RequestBody List<OpeningHoursPayload> openingHoursPayload) {
        this.restaurantService.saveOpeningHours(openingHoursPayload);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/opening")
    public ResponseEntity<List<OpeningHoursApi>> getOpeningHours() {
        List<OpeningHours> openingHours = this.restaurantService.getOpeningHours();
        List<OpeningHoursApi> result = OpeningHoursMapper.mapToOpeningHoursApi(openingHours);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/opening/update")
    public ResponseEntity<String> updateOpeningHours(
            @RequestBody List<OpeningHoursPayload> openingHoursPayload) {
        this.restaurantService.updateOpeningHours(openingHoursPayload);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/item/add")
    public ResponseEntity<String> addItem(@RequestBody ItemPayload itemPayload) {
        this.restaurantService.saveItem(itemPayload);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemApi>> getItems() {
        List<Item> items = this.restaurantService.getItems()//
                .stream()//
                .collect(Collectors.toList());
        List<ItemApi> result = ItemMapper.mapToItemApi(items);
        return ResponseEntity.ok(result);
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

    @GetMapping("/bestellungen")
    public ResponseEntity<List<BestellungApi>> getBestellungen() {
        List<Bestellung> bestellungs = this.bestellungService.getRestaurantBestellungen();
        List<BestellungApi> result = BestellungMapper.mapToBestellungApi(bestellungs);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/bestellung/confirm/{bestellungId}")
    public ResponseEntity<List<BestellungApi>> confirmBestellung(@PathVariable Long bestellungId) {
        this.bestellungService.confirmBestellung(bestellungId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bestellung/stornieren/{bestellungId}")
    public ResponseEntity<List<BestellungApi>> stornierenBestellung(@PathVariable Long bestellungId) {
        this.bestellungService.stornierenBestellung(bestellungId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bestellung/complete/{bestellungId}")
    public ResponseEntity<List<BestellungApi>> completeBestellung(@PathVariable Long bestellungId) {
        this.bestellungService.stornierenBestellung(bestellungId);
        return ResponseEntity.ok().build();
    }
}
