package com.uni.lieferspatz.service.mapper;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.dto.api.AvailableRestaurantApi;

public class RestaurantMapper {
    public static AvailableRestaurantApi mapToAvailableRestaurantApi(Restaurant restaurant) {
        AvailableRestaurantApi restaurantApi = new AvailableRestaurantApi();
        restaurantApi.setId(restaurant.getId());
        restaurantApi.setName(restaurant.getName());
        restaurantApi.setStrasse(restaurant.getStrasse());
        restaurantApi.setPlz(restaurant.getPlz());
        restaurantApi.setOrt(restaurant.getOrt());
        restaurantApi.setBeschreibung(restaurant.getBeschreibung());
        if (restaurant.getImage() != null) {
            restaurantApi
                    .setImage("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(restaurant.getImage()));
        }
        return restaurantApi;
    }

    public static List<AvailableRestaurantApi> mapToAvailableRestaurantApi(List<Restaurant> restaurants) {
        return restaurants.stream()//
                .map(RestaurantMapper::mapToAvailableRestaurantApi)//
                .collect(Collectors.toList());
    }
}
