package com.uni.lieferspatz.service;

import org.springframework.stereotype.Service;

import com.uni.lieferspatz.domain.Item;
import com.uni.lieferspatz.payload.ItemPayload;
import com.uni.lieferspatz.repository.ItemRepository;
import com.uni.lieferspatz.service.exception.ResourceException;
import com.uni.lieferspatz.service.mapper.ItemMapper;

@Service
public class RestaurantService {
    private final ItemRepository itemRepository;

    public RestaurantService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void saveItem(Long restaurantId, ItemPayload itemPayload) {
        // TODO: get restaurantId from from jwt token
        Item item = ItemMapper.mapFromPayload(restaurantId, itemPayload);
        try {
            this.itemRepository.save(item);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceException("Fehler beim Hinzufügen von Item");
        }
    }

    public void deleteItem(Long itemId) {
        // TODO: get restaurantId from from jwt token
        try {
            this.itemRepository.deleteById(itemId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceException("Fehler beim Hinzufügen von Item");
        }
    }

    public void updateItem(ItemPayload itemPayload) {
        // TODO: get restaurantId from from jwt token
        Item item = this.itemRepository.findById(itemPayload.getId())
                .orElseThrow(() -> new ResourceException("Item nicht gefunden"));
        this.saveItem(item.getRestaurant().getId(), itemPayload);
    }
}
