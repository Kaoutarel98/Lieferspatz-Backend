package com.uni.lieferspatz.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.uni.lieferspatz.domain.Item;
import com.uni.lieferspatz.domain.OpeningHours;
import com.uni.lieferspatz.payload.ItemPayload;
import com.uni.lieferspatz.payload.OpeningHoursPayload;
import com.uni.lieferspatz.repository.ItemRepository;
import com.uni.lieferspatz.repository.OpeningHoursRepository;
import com.uni.lieferspatz.service.exception.ResourceException;
import com.uni.lieferspatz.service.mapper.ItemMapper;
import com.uni.lieferspatz.service.mapper.OpeningHoursMapper;

@Service
public class RestaurantService {
    private final ItemRepository itemRepository;
    private final OpeningHoursRepository openingHoursRepository;

    public RestaurantService(ItemRepository itemRepository, OpeningHoursRepository openingHoursRepository) {
        this.itemRepository = itemRepository;
        this.openingHoursRepository = openingHoursRepository;
    }

    public void saveOpeningHours(Long restaurantId, List<OpeningHoursPayload> openingHoursPayload) {
        Set<OpeningHours> openingHours = OpeningHoursMapper.mapFromPayload(restaurantId, openingHoursPayload);
        try {
            this.openingHoursRepository.saveAll(openingHours);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceException("Fehler beim Speichern von Öffnungszeiten");
        }
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
            throw new ResourceException("Fehler beim Löschen von Item");
        }
    }

    public void updateItem(ItemPayload itemPayload) {
        // TODO: get restaurantId from from jwt token
        Item item = this.itemRepository.findById(itemPayload.getId())
                .orElseThrow(() -> new ResourceException("Item nicht gefunden"));
        this.saveItem(item.getRestaurant().getId(), itemPayload);
    }
}
