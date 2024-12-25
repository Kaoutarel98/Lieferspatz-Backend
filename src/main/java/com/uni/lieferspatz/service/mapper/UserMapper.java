package com.uni.lieferspatz.service.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.uni.lieferspatz.domain.Kunde;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.domain.User;
import com.uni.lieferspatz.payload.KundePayload;
import com.uni.lieferspatz.payload.RestaurantPayload;
import com.uni.lieferspatz.payload.UserPayload;

public class UserMapper {

    private static void mapUser(User user, UserPayload userPayload, PasswordEncoder passwordEncoder) {
        user.setEmail(userPayload.getEmail().toLowerCase());
        user.setStrasse(userPayload.getStrasse());
        user.setPlz(userPayload.getPlz());
        user.setOrt(userPayload.getOrt());
        String encodedPassword = passwordEncoder.encode(userPayload.getPassword());
        user.setPassword(encodedPassword);
    }

    public static Kunde mapKunde(KundePayload kundePayload, PasswordEncoder passwordEncoder) {
        Kunde newKunde = new Kunde();
        mapUser(newKunde, kundePayload, passwordEncoder);
        newKunde.setVorname(kundePayload.getVorname());
        newKunde.setNachname(kundePayload.getNachname());
        return newKunde;
    }

    public static Restaurant mapRestaurant(RestaurantPayload restaurantPayload, PasswordEncoder passwordEncoder) {
        Restaurant newRestaurant = new Restaurant();
        mapUser(newRestaurant, restaurantPayload, passwordEncoder);

        newRestaurant.setName(restaurantPayload.getName());
        newRestaurant.setBeschreibung(restaurantPayload.getBeschreibung());
        newRestaurant.setImage(restaurantPayload.getImage());
        return newRestaurant;
    }
}
