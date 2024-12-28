package com.uni.lieferspatz.service.mapper;

import java.math.BigDecimal;

import com.uni.lieferspatz.constants.RolesConstants;
import com.uni.lieferspatz.domain.Kunde;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.domain.User;
import com.uni.lieferspatz.dto.api.KundeApi;
import com.uni.lieferspatz.dto.api.RestaurantApi;
import com.uni.lieferspatz.dto.api.UserApi;
import com.uni.lieferspatz.dto.payload.KundePayload;
import com.uni.lieferspatz.dto.payload.RestaurantPayload;
import com.uni.lieferspatz.dto.payload.UserPayload;

public class UserMapper {
    private static void mapUser(User user, UserPayload userPayload) {
        user.setEmail(userPayload.getEmail().toLowerCase());
        user.setStrasse(userPayload.getStrasse());
        user.setPlz(userPayload.getPlz());
        user.setOrt(userPayload.getOrt());
        user.setPassword(userPayload.getPassword());
    }

    public static Kunde mapFromKundePayload(KundePayload kundePayload) {
        Kunde newKunde = new Kunde();
        mapUser(newKunde, kundePayload);
        newKunde.setVorname(kundePayload.getVorname());
        newKunde.setNachname(kundePayload.getNachname());
        newKunde.setGeldbeutel(BigDecimal.valueOf(100));
        return newKunde;
    }

    public static Restaurant mapFromRestaurantPayload(RestaurantPayload restaurantPayload) {
        Restaurant newRestaurant = new Restaurant();
        mapUser(newRestaurant, restaurantPayload);
        newRestaurant.setName(restaurantPayload.getName());
        newRestaurant.setBeschreibung(restaurantPayload.getBeschreibung());
        newRestaurant.setImage(restaurantPayload.getImage());
        newRestaurant.setGeldbeutel(BigDecimal.ZERO);
        return newRestaurant;
    }

    private static void mapToUserApi(User user, UserApi userApi) {
        userApi.setStrasse(user.getStrasse());
        userApi.setPlz(user.getPlz());
        userApi.setOrt(user.getOrt());
    }

    public static KundeApi mapToKundeApi(Kunde kunde) {
        KundeApi kundeApi = new KundeApi();
        mapToUserApi(kunde, kundeApi);
        kundeApi.setEmail(kunde.getEmail());
        kundeApi.setVorname(kunde.getVorname());
        kundeApi.setNachname(kunde.getNachname());
        kundeApi.setGeldbeutel(kunde.getGeldbeutel().doubleValue());
        kundeApi.setVorgemerkt(kunde.getVorgemerkt().doubleValue());
        return kundeApi;
    }

    public static RestaurantApi mapToRestaurantApi(Restaurant restaurant) {
        RestaurantApi restaurantApi = new RestaurantApi();
        mapToUserApi(restaurant, restaurantApi);
        restaurantApi.setEmail(restaurant.getEmail());
        restaurantApi.setName(restaurant.getName());
        restaurantApi.setBeschreibung(restaurant.getBeschreibung());
        restaurantApi.setImage(restaurant.getImage());
        restaurantApi.setGeldbeutel(restaurant.getGeldbeutel().doubleValue());
        return restaurantApi;
    }

    public static UserApi mapToUserApi(User user) {
        if (RolesConstants.KUNDE.equals(user.getRole())) {
            return mapToKundeApi((Kunde) user);
        } else if (RolesConstants.RESTAURANT.equals(user.getRole())) {
            return mapToRestaurantApi((Restaurant) user);
        }
        return null;
    }
}
