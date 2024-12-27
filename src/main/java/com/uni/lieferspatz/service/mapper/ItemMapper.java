package com.uni.lieferspatz.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.uni.lieferspatz.domain.Item;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.dto.api.ItemApi;
import com.uni.lieferspatz.dto.payload.ItemPayload;

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

    public static ItemApi mapToItemApi(Item item) {
        ItemApi itemApi = new ItemApi();
        itemApi.setId(item.getId());
        itemApi.setName(item.getName());
        itemApi.setBeschreibung(item.getBeschreibung());
        itemApi.setPreis(item.getPreis());
        itemApi.setImageUrl(item.getImageUrl());
        return itemApi;
    }

    public static List<ItemApi> mapToItemApi(List<Item> items) {
        return items.stream().map(ItemMapper::mapToItemApi).collect(Collectors.toList());
    }
}
