package com.uni.lieferspatz.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uni.lieferspatz.domain.Item;
import com.uni.lieferspatz.domain.LieferPlz;
import com.uni.lieferspatz.domain.OpeningHours;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.dto.payload.ItemPayload;
import com.uni.lieferspatz.dto.payload.LieferPlzPayload;
import com.uni.lieferspatz.dto.payload.OpeningHoursPayload;
import com.uni.lieferspatz.repository.ItemRepository;
import com.uni.lieferspatz.repository.LieferPlzRepository;
import com.uni.lieferspatz.repository.OpeningHoursRepository;
import com.uni.lieferspatz.repository.RestaurantRepository;
import com.uni.lieferspatz.service.auth.SecurityUtils;
import com.uni.lieferspatz.service.exception.ResourceException;
import com.uni.lieferspatz.service.mapper.ItemMapper;
import com.uni.lieferspatz.service.mapper.LieferPlzMapper;
import com.uni.lieferspatz.service.mapper.OpeningHoursMapper;

@Service
@Transactional
public class RestaurantService {
    private final ItemRepository itemRepository;
    private final OpeningHoursRepository openingHoursRepository;
    private final LieferPlzRepository lieferPlzRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(ItemRepository itemRepository, OpeningHoursRepository openingHoursRepository,
            LieferPlzRepository lieferPlzRepository, RestaurantRepository restaurantRepository) {
        this.itemRepository = itemRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.lieferPlzRepository = lieferPlzRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void savePlz(LieferPlzPayload plz) {
        this.getCurrentAccount().ifPresentOrElse(user -> {
            Set<LieferPlz> lieferPlz = LieferPlzMapper.mapFromPayloadToList(user.getId(), plz);
            try {
                this.lieferPlzRepository.deleteByRestaurantId(user.getId());
                this.lieferPlzRepository.saveAll(lieferPlz);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ResourceException("Fehler beim Speichern von PLZs");
            }
        }, () -> {
            throw new ResourceException("Fehler beim Speichern von PLZs");
        });
    }

    public void saveOpeningHours(List<OpeningHoursPayload> openingHoursPayload) {
        this.getCurrentAccount().ifPresentOrElse(user -> {
            this.saveOpeningHoursToRepo(user.getId(), openingHoursPayload);
        }, () -> {
            throw new ResourceException("Fehler beim Speichern von Öffnungszeiten");
        });
    }

    public void updateOpeningHours(List<OpeningHoursPayload> openingHoursPayload) {
        this.getCurrentAccount().ifPresentOrElse(user -> {
            this.validateOpeningHoursVsUserId(openingHoursPayload, user.getId());
            this.saveOpeningHoursToRepo(user.getId(), openingHoursPayload);
        }, () -> {
            throw new ResourceException("Fehler beim Speichern von Öffnungszeiten");
        });
    }

    private void validateOpeningHoursVsUserId(List<OpeningHoursPayload> openingHoursPayload, Long restaurantId) {
        List<Long> ids = OpeningHoursMapper.mapToIds(openingHoursPayload);
        List<OpeningHours> existingOpeningHours = this.openingHoursRepository.findAllByIdIn(ids);
        List<OpeningHours> invalidPayloads = existingOpeningHours.stream()
                .filter(item -> !item.getRestaurant().getId().equals(restaurantId))
                .collect(Collectors.toList());
        if (!invalidPayloads.isEmpty()) {
            throw new ResourceException("Fehler beim Speichern von Öffnungszeiten");
        }
    }

    private void saveOpeningHoursToRepo(Long restaurantId, List<OpeningHoursPayload> openingHoursPayload) {
        Set<OpeningHours> openingHours = OpeningHoursMapper.mapFromPayload(restaurantId, openingHoursPayload);
        try {
            this.openingHoursRepository.saveAll(openingHours);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceException("Fehler beim Speichern von Öffnungszeiten");
        }
    }

    public Restaurant getRestaurant(Long restaurantId) {
        return this.restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceException("Restaurant nicht gefunden"));
    }

    public void saveItem(ItemPayload itemPayload) {
        this.getCurrentAccount().ifPresentOrElse(user -> {
            this.saveItemToRepo(user.getId(), itemPayload);
        }, () -> {
            throw new ResourceException("Fehler beim Hinzufügen von Item: Kein Benutzer angemeldet");
        });
    }

    private void saveItemToRepo(Long restaurantId, ItemPayload itemPayload) {
        Item item = ItemMapper.mapFromPayload(restaurantId, itemPayload);
        try {
            this.itemRepository.save(item);
        } catch (Exception e) {
            throw new ResourceException("Fehler beim Hinzufügen eines neuen Items", e);
        }
    }

    public void deleteItem(Long itemId) {
        this.getCurrentAccount().ifPresent(user -> {
            this.itemRepository.findByIdAndRestaurantId(itemId, user.getId()).ifPresentOrElse(item -> {
                try {
                    this.itemRepository.delete(item);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ResourceException("Fehler beim Löschen von Item");
                }
            }, () -> {
                throw new ResourceException("Item nicht gefunden");
            });
        });
    }

    public void updateItem(ItemPayload itemPayload) {
        if (itemPayload.getId() == null) {
            throw new ResourceException("Item nicht gefunden");
        }
        this.getCurrentAccount().ifPresentOrElse(user -> {
            this.itemRepository.findByIdAndRestaurantId(itemPayload.getId(), user.getId())//
                    .ifPresentOrElse(existingItem -> {
                        saveItemToRepo(user.getId(), itemPayload);
                    }, () -> {
                        throw new ResourceException("Item nicht gefunden");
                    });
        }, () -> {
            throw new ResourceException("Fehler beim Hinzufügen von Item: Kein Benutzer angemeldet");
        });
    }

    public Optional<Restaurant> getCurrentAccount() {
        return SecurityUtils.getCurrentAccountEmail().flatMap(this.restaurantRepository::findOneByEmailIgnoreCase);
    }
}
