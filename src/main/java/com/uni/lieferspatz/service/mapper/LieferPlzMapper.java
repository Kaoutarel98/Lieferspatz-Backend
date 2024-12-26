package com.uni.lieferspatz.service.mapper;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.uni.lieferspatz.domain.LieferPlz;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.payload.LieferPlzPayload;

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
        return Arrays.asList(lieferPlzPayload.getPlz().split(",")).stream()
                .map(plz -> mapFromPayload(restaurantId, plz))
                .collect(Collectors.toSet());
    }
}
