package com.uni.lieferspatz.service.mapper;

import com.uni.lieferspatz.domain.Item;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.payload.ItemPayload;

public class ItemMapper {
    public static Item mapFromPayload(Long restaurantId, ItemPayload itemPayload) {
        Item item = new Item();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        item.setRestaurant(restaurant);
        item.setId(itemPayload.getId());
        item.setName(itemPayload.getName());
        item.setBeschreibung(itemPayload.getBeschreibung());
        item.setPreis(itemPayload.getPreis());
        item.setImageUrl(itemPayload.getImageUrl());
        return item;
    }

    public static ItemPayload mapToPayload(Item item) {
        ItemPayload itemPayload = new ItemPayload();
        itemPayload.setId(item.getId());
        itemPayload.setName(item.getName());
        itemPayload.setBeschreibung(item.getBeschreibung());
        itemPayload.setPreis(item.getPreis());
        itemPayload.setImageUrl(item.getImageUrl());
        return itemPayload;
    }
}
