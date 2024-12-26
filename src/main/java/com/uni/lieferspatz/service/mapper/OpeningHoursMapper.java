package com.uni.lieferspatz.service.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.uni.lieferspatz.domain.OpeningHours;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.dto.payload.OpeningHoursPayload;

public class OpeningHoursMapper {

    public static OpeningHours mapFromPayload(Long restaurantId, OpeningHoursPayload openingHoursPayload) {
        OpeningHours openingHours = new OpeningHours();
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        openingHours.setId(openingHoursPayload.getId());
        openingHours.setRestaurant(restaurant);
        openingHours.setDayOfWeek(openingHoursPayload.getDayOfWeek());
        openingHours.setOpenTime(openingHoursPayload.getOpenTime());
        openingHours.setCloseTime(openingHoursPayload.getCloseTime());
        return openingHours;
    }

    public static Set<OpeningHours> mapFromPayload(Long restaurantId, List<OpeningHoursPayload> openingHoursPayload) {
        return openingHoursPayload.stream()//
                .map(openingHours -> mapFromPayload(restaurantId, openingHours))//
                .collect(Collectors.toSet());
    }

    public static OpeningHours mapToPayload(OpeningHours openingHours) {
        OpeningHoursPayload openingHoursPayload = new OpeningHoursPayload();
        openingHoursPayload.setId(openingHours.getId());
        openingHours.setDayOfWeek(openingHoursPayload.getDayOfWeek());
        openingHours.setOpenTime(openingHoursPayload.getOpenTime());
        openingHours.setCloseTime(openingHoursPayload.getCloseTime());
        return openingHours;
    }

    public static List<Long> mapToIds(List<OpeningHoursPayload> openingHoursPayload) {
        return openingHoursPayload.stream()//
                .map(OpeningHoursPayload::getId)//
                .collect(Collectors.toList());
    }
}
