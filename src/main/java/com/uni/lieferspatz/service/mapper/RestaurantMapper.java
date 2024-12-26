package com.uni.lieferspatz.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.dto.api.AvailableRestaurantApi;

public class RestaurantMapper {

    public static AvailableRestaurantApi mapToAvailableRestaurantApi(Restaurant restaurant) {
        AvailableRestaurantApi restaurantPayload = new AvailableRestaurantApi();
        restaurantPayload.setId(restaurant.getId());
        restaurantPayload.setName(restaurant.getName());
        restaurantPayload.setStrasse(restaurant.getStrasse());
        restaurantPayload.setPlz(restaurant.getPlz());
        restaurantPayload.setOrt(restaurant.getOrt());
        restaurantPayload.setBeschreibung(restaurant.getBeschreibung());
        return restaurantPayload;
    }

    public static List<AvailableRestaurantApi> mapToAvailableRestaurantApi(List<Restaurant> restaurants) {
        return restaurants.stream()//
                .map(RestaurantMapper::mapToAvailableRestaurantApi)//
                .collect(Collectors.toList());
    }
}
