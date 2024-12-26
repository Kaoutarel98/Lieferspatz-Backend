package com.uni.lieferspatz.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uni.lieferspatz.domain.Item;
import com.uni.lieferspatz.domain.LieferPlz;
import com.uni.lieferspatz.domain.OpeningHours;
import com.uni.lieferspatz.payload.ItemPayload;
import com.uni.lieferspatz.payload.LieferPlzPayload;
import com.uni.lieferspatz.payload.OpeningHoursPayload;
import com.uni.lieferspatz.repository.ItemRepository;
import com.uni.lieferspatz.repository.LieferPlzRepository;
import com.uni.lieferspatz.repository.OpeningHoursRepository;
import com.uni.lieferspatz.service.exception.ResourceException;
import com.uni.lieferspatz.service.mapper.ItemMapper;
import com.uni.lieferspatz.service.mapper.LieferPlzMapper;
import com.uni.lieferspatz.service.mapper.OpeningHoursMapper;

@Service
public class RestaurantService {
    private final ItemRepository itemRepository;
    private final OpeningHoursRepository openingHoursRepository;
    private final LieferPlzRepository lieferPlzRepository;

    public RestaurantService(ItemRepository itemRepository, OpeningHoursRepository openingHoursRepository,
            LieferPlzRepository lieferPlzRepository) {
        this.itemRepository = itemRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.lieferPlzRepository = lieferPlzRepository;
    }

    @Transactional
    public void savePlz(Long restaurantId, LieferPlzPayload plz) {
        Set<LieferPlz> lieferPlz = LieferPlzMapper.mapFromPayloadToList(restaurantId, plz);
        try {
            this.lieferPlzRepository.deleteByRestaurantId(restaurantId);
            this.lieferPlzRepository.saveAll(lieferPlz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceException("Fehler beim Speichern von Öffnungszeiten");
        }
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
