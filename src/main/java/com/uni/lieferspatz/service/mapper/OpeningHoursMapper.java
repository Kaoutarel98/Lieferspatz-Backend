package com.uni.lieferspatz.service.mapper;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.uni.lieferspatz.domain.OpeningHours;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.dto.api.OpeningHoursApi;
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

    public static OpeningHoursApi mapToOpeningHoursApi(OpeningHours openingHours) {
        OpeningHoursApi openingHoursApi = new OpeningHoursApi();
        openingHoursApi.setId(openingHours.getId());
        openingHoursApi.setDayOfWeek(openingHours.getDayOfWeek());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        openingHoursApi.setOpenTime(openingHours.getOpenTime().format(formatter));
        openingHoursApi.setCloseTime(openingHours.getCloseTime().format(formatter));
        return openingHoursApi;
    }

    public static List<OpeningHoursApi> mapToOpeningHoursApi(List<OpeningHours> openingHours) {
        return openingHours.stream()//
                .map(openingHour -> mapToOpeningHoursApi(openingHour))//
                .collect(Collectors.toList());
    }

    public static List<Long> mapToIds(List<OpeningHoursPayload> openingHoursPayload) {
        return openingHoursPayload.stream()//
                .map(OpeningHoursPayload::getId)//
                .collect(Collectors.toList());
    }
}
