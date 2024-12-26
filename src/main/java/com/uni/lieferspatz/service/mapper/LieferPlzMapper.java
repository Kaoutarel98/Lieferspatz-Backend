package com.uni.lieferspatz.service.mapper;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.uni.lieferspatz.domain.LieferPlz;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.dto.payload.LieferPlzPayload;

public class LieferPlzMapper {

    private static LieferPlz mapFromPayload(Long restaurantId, String lieferPlzPayload) {
        LieferPlz lieferPlz = new LieferPlz();
        lieferPlz.setPlz(lieferPlzPayload);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        lieferPlz.setRestaurant(restaurant);
        return lieferPlz;
    }

    public static Set<LieferPlz> mapFromPayloadToList(Long restaurantId, LieferPlzPayload lieferPlzPayload) {
        return Arrays.stream(lieferPlzPayload.getPlz().split(",")) //
                .map(String::trim) //
                .filter(StringUtils::hasText) //
                .distinct() //
                .map(plz -> mapFromPayload(restaurantId, plz)) //
                .collect(Collectors.toSet());
    }
}
