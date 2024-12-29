package com.uni.lieferspatz.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uni.lieferspatz.domain.Item;
import com.uni.lieferspatz.domain.Kunde;
import com.uni.lieferspatz.domain.WarenkorbItem;
import com.uni.lieferspatz.domain.WarenkorbKey;
import com.uni.lieferspatz.dto.payload.WarenkorbItemPayload;
import com.uni.lieferspatz.repository.ItemRepository;
import com.uni.lieferspatz.repository.WarenkorbRepository;
import com.uni.lieferspatz.service.exception.ResourceException;
import com.uni.lieferspatz.service.exception.ResourceNotFoundException;

@Service
public class WarenkorbService {
    private final KundeService kundeService;
    private final WarenkorbRepository warenkorbRepository;
    private final ItemRepository itemRepository;

    public WarenkorbService(KundeService kundeService, WarenkorbRepository warenkorbRepository,
            ItemRepository itemRepository) {
        this.kundeService = kundeService;
        this.warenkorbRepository = warenkorbRepository;
        this.itemRepository = itemRepository;
    }

    public List<WarenkorbItem> getWarenkorbItems() {
        return this.kundeService.getCurrentAccount()
                .map(Kunde::getWarenkorbItems)
                .orElseGet(ArrayList::new);
    }

    @Transactional
    public void addOrUpdateWarenkorbItem(WarenkorbItemPayload warenkorbItemPayload) {
        if (warenkorbItemPayload == null || warenkorbItemPayload.getItemId() == null) {
            throw new ResourceException("WarenkorbItemPayload oder Artikel-ID dürfen nicht null sein");
        }
        this.kundeService.getCurrentAccount().ifPresent(kunde -> {
            List<WarenkorbItem> warenkorbsItems = kunde.getWarenkorbItems();
            Item item = this.itemRepository.findById(warenkorbItemPayload.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Artikel mit der angegebenen ID existiert nicht"));
            int quantity = warenkorbItemPayload.getQuantity();
            if (!warenkorbsItems.isEmpty()) {
                // check if the item is from the same restaurant
                Long currentRestaurantId = warenkorbsItems.get(0).getItem().getRestaurant().getId();
                if (!item.getRestaurant().getId().equals(currentRestaurantId)) {
                    kunde.getWarenkorbItems().clear();
                }
            }
            warenkorbsItems.stream() //
                    .filter(warentkorbItem -> warentkorbItem.getItem().getId()
                            .equals(warenkorbItemPayload.getItemId()))
                    .findFirst()
                    .ifPresentOrElse(warentkorbItem -> {
                        if (quantity <= 0) {
                            warenkorbsItems.remove(warentkorbItem);
                        } else {
                            warentkorbItem.setQuantity(quantity);
                            warentkorbItem.setAddedAt(LocalDateTime.now());
                        }
                    }, () -> {
                        if (quantity > 0) {
                            this.saveNewWarenkorbItem(kunde, item, quantity);
                        }
                    });
        });
    }

    private void saveNewWarenkorbItem(Kunde kunde, Item item, int quantity) {
        WarenkorbKey warenkorbKey = new WarenkorbKey(kunde.getId(), item.getId());
        WarenkorbItem warenkorb = new WarenkorbItem();
        warenkorb.setId(warenkorbKey);
        warenkorb.setAddedAt(LocalDateTime.now());
        warenkorb.setQuantity(quantity);
        warenkorb.setItem(item);
        warenkorb.setKunde(kunde);
        this.warenkorbRepository.save(warenkorb);
    }

    @Transactional
    public void deleteByItemId(Long itemId) {
        this.kundeService.getCurrentAccount()
                .ifPresent(kunde -> {
                    List<WarenkorbItem> warenkorbsItems = kunde.getWarenkorbItems();
                    warenkorbsItems.stream() //
                            .filter(warentkorbItem -> warentkorbItem.getItem().getId()
                                    .equals(itemId))
                            .findFirst()
                            .ifPresent(warentkorbItem -> {
                                warenkorbsItems.remove(warentkorbItem);
                            });
                });
    }
}
