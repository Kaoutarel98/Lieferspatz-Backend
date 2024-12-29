package com.uni.lieferspatz.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uni.lieferspatz.domain.Item;
import com.uni.lieferspatz.domain.LieferPlz;
import com.uni.lieferspatz.domain.OpeningHours;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.dto.api.BestellungApi;
import com.uni.lieferspatz.dto.payload.ItemPayload;
import com.uni.lieferspatz.dto.payload.LieferPlzPayload;
import com.uni.lieferspatz.dto.payload.OpeningHoursPayload;
import com.uni.lieferspatz.repository.ItemRepository;
import com.uni.lieferspatz.repository.LieferPlzRepository;
import com.uni.lieferspatz.repository.OpeningHoursRepository;
import com.uni.lieferspatz.repository.RestaurantRepository;
import com.uni.lieferspatz.service.auth.SecurityUtils;
import com.uni.lieferspatz.service.exception.ResourceException;
import com.uni.lieferspatz.service.exception.ResourceNotFoundException;
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
    private final PasswordEncoder passwordEncoder;
    private final SimpMessagingTemplate messagingTemplate;

    public RestaurantService(ItemRepository itemRepository, OpeningHoursRepository openingHoursRepository,
            LieferPlzRepository lieferPlzRepository, RestaurantRepository restaurantRepository,
            PasswordEncoder passwordEncoder, SimpMessagingTemplate messagingTemplate) {
        this.itemRepository = itemRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.lieferPlzRepository = lieferPlzRepository;
        this.restaurantRepository = restaurantRepository;
        this.passwordEncoder = passwordEncoder;
        this.messagingTemplate = messagingTemplate;
    }

    public void saveNeueRestaurant(Restaurant kunde) {
        String encodedPassword = this.passwordEncoder.encode(kunde.getPassword());
        kunde.setPassword(encodedPassword);
        this.restaurantRepository.save(kunde);
    }

    public void notifyRestaurant(String restaurantEmail, BestellungApi bestellungApi) {
        this.messagingTemplate.convertAndSendToUser(restaurantEmail, "/queue/notifications", bestellungApi);
    }

    public void savePlz(LieferPlzPayload plz) {
        this.getCurrentAccount().ifPresentOrElse(user -> {
            Set<LieferPlz> lieferPlz = LieferPlzMapper.mapFromPayloadToList(user.getId(), plz);
            try {
                this.lieferPlzRepository.deleteByRestaurantId(user.getId());
                this.lieferPlzRepository.saveAll(lieferPlz);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ResourceException(
                        "Fehler beim Speichern der Postleitzahlen für Restaurant-ID: " + user.getId(), e);
            }
        }, () -> {
            throw new ResourceException("Fehler beim Speichern der Postleitzahlen: Kein Benutzer eingeloggt");
        });
    }

    public void saveOpeningHours(List<OpeningHoursPayload> openingHoursPayload) {
        this.getCurrentAccount().ifPresentOrElse(user -> {
            this.saveOpeningHoursToRepo(user.getId(), openingHoursPayload);
        }, () -> {
            throw new ResourceException("Fehler beim Speichern der Öffnungszeiten: Kein Benutzer eingeloggt");
        });
    }

    public void updateOpeningHours(List<OpeningHoursPayload> openingHoursPayload) {
        this.getCurrentAccount().ifPresentOrElse(user -> {
            this.validateOpeningHoursVsUserId(openingHoursPayload, user.getId());
            this.saveOpeningHoursToRepo(user.getId(), openingHoursPayload);
        }, () -> {
            throw new ResourceException("Fehler beim Aktualisieren der Öffnungszeiten: Kein Benutzer eingeloggt");
        });
    }

    private void validateOpeningHoursVsUserId(List<OpeningHoursPayload> openingHoursPayload, Long restaurantId) {
        List<Long> ids = OpeningHoursMapper.mapToIds(openingHoursPayload);
        List<OpeningHours> existingOpeningHours = this.openingHoursRepository.findAllByIdIn(ids);
        List<OpeningHours> invalidPayloads = existingOpeningHours.stream()
                .filter(item -> !item.getRestaurant().getId().equals(restaurantId))
                .collect(Collectors.toList());
        if (!invalidPayloads.isEmpty()) {
            throw new ResourceException("Fehler beim Aktualisieren der Öffnungszeiten: Ungültige Restaurant-ID");
        }
    }

    private void saveOpeningHoursToRepo(Long restaurantId, List<OpeningHoursPayload> openingHoursPayload) {
        Set<OpeningHours> openingHours = OpeningHoursMapper.mapFromPayload(restaurantId, openingHoursPayload);
        try {
            this.openingHoursRepository.saveAll(openingHours);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceException("Fehler beim Speichern der Öffnungszeiten für Restaurant-ID: " + restaurantId,
                    e);
        }
    }

    public Restaurant getRestaurant(Long restaurantId) {
        return this.restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant nicht gefunden mit ID: " + restaurantId));
    }

    public void saveItem(ItemPayload itemPayload) {
        this.getCurrentAccount().ifPresentOrElse(user -> {
            this.saveItemToRepo(user.getId(), itemPayload);
        }, () -> {
            throw new ResourceException("Fehler beim Hinzufügen des Artikels: Kein Benutzer eingeloggt");
        });
    }

    private void saveItemToRepo(Long restaurantId, ItemPayload itemPayload) {
        Item item = ItemMapper.mapFromPayload(restaurantId, itemPayload);
        try {
            this.itemRepository.save(item);
        } catch (Exception e) {
            throw new ResourceException(
                    "Fehler beim Hinzufügen eines neuen Artikels für Restaurant-ID: " + restaurantId, e);
        }
    }

    public void deleteItem(Long itemId) {
        this.getCurrentAccount().ifPresent(user -> {
            this.itemRepository.findByIdAndRestaurantId(itemId, user.getId()).ifPresentOrElse(item -> {
                try {
                    this.itemRepository.delete(item);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ResourceException("Fehler beim Löschen des Artikels mit ID: " + itemId, e);
                }
            }, () -> {
                throw new ResourceNotFoundException("Artikel nicht gefunden mit ID: " + itemId);
            });
        });
    }

    public void updateItem(ItemPayload itemPayload) {
        if (itemPayload.getId() == null) {
            throw new ResourceException("Artikel-ID fehlt");
        }
        this.getCurrentAccount().ifPresentOrElse(user -> {
            this.itemRepository.findByIdAndRestaurantId(itemPayload.getId(), user.getId())//
                    .ifPresentOrElse(existingItem -> {
                        saveItemToRepo(user.getId(), itemPayload);
                    }, () -> {
                        throw new ResourceNotFoundException("Artikel nicht gefunden mit ID: " + itemPayload.getId());
                    });
        }, () -> {
            throw new ResourceException("Fehler beim Aktualisieren des Artikels: Kein Benutzer eingeloggt");
        });
    }

    public Optional<Restaurant> getCurrentAccount() {
        return SecurityUtils.getCurrentAccountEmail().flatMap(this.restaurantRepository::findOneByEmailIgnoreCase);
    }
}
