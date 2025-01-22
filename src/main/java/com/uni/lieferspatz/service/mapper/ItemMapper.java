package com.uni.lieferspatz.service.mapper;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

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
        String base64Image = itemPayload.getImageUrl();
        if (StringUtils.hasText(base64Image)) {
            if (base64Image.contains(",")) {
                base64Image = base64Image.split(",")[1];
            }
            item.setImageUrl(Base64.getDecoder().decode(base64Image));
        }
        return item;
    }

    public static ItemApi mapToItemApi(Item item) {
        ItemApi itemApi = new ItemApi();
        itemApi.setId(item.getId());
        itemApi.setName(item.getName());
        itemApi.setBeschreibung(item.getBeschreibung());
        itemApi.setPreis(item.getPreis());
        if (item.getImageUrl() != null) {
            itemApi.setImageUrl("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(item.getImageUrl()));
        }
        return itemApi;
    }

    public static List<ItemApi> mapToItemApi(List<Item> items) {
        return items.stream().map(ItemMapper::mapToItemApi).collect(Collectors.toList());
    }
}
