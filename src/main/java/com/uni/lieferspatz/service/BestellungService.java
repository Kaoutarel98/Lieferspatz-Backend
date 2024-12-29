package com.uni.lieferspatz.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uni.lieferspatz.constants.BestellStatus;
import com.uni.lieferspatz.domain.Bestellung;
import com.uni.lieferspatz.domain.BestellungItem;
import com.uni.lieferspatz.domain.Kunde;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.domain.WarenkorbItem;
import com.uni.lieferspatz.dto.api.BestellungApi;
import com.uni.lieferspatz.service.exception.ResourceException;
import com.uni.lieferspatz.service.exception.ResourceNotFoundException;
import com.uni.lieferspatz.service.mapper.BestellungMapper;

import jakarta.persistence.EntityManager;

@Service
public class BestellungService {
    private final KundeService kundeService;
    private final RestaurantService restaurantService;
    private final EntityManager entityManager;

    public BestellungService(KundeService kundeService, RestaurantService restaurantService,
            EntityManager entityManager) {
        this.kundeService = kundeService;
        this.restaurantService = restaurantService;
        this.entityManager = entityManager;
    }

    @Transactional
    public void saveBestellung() {
        this.kundeService.getCurrentAccount().ifPresentOrElse(kunde -> {
            List<WarenkorbItem> warenkorbItems = kunde.getWarenkorbItems();
            if (warenkorbItems.isEmpty()) {
                throw new ResourceException(
                        "Der Warenkorb ist leer. Bitte fügen Sie Artikel hinzu, bevor Sie eine Bestellung aufgeben.");
            }
            Bestellung bestellung = new Bestellung();
            List<BestellungItem> bestellungItems = this.mapToBestellungItems(bestellung, warenkorbItems);
            bestellung.setBestellungItems(bestellungItems);
            BigDecimal gesamtpreis = bestellungItems.stream()
                    .map(BestellungItem::getTotalPreis)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (kunde.getGeldbeutel().subtract(kunde.getVorgemerkt()).compareTo(gesamtpreis) < 0) {
                throw new ResourceException("Nicht genügend Guthaben im Geldbeutel, um die Bestellung zu bezahlen.");
            }
            Restaurant restaurant = warenkorbItems.get(0).getItem().getRestaurant();
            bestellung.setRestaurant(restaurant);
            bestellung.setStatus(BestellStatus.BEARBEITUNG);
            bestellung.setLieferAdresse(String.format("%s, %s %s", kunde.getStrasse(),
                    kunde.getPlz(), kunde.getOrt()));
            bestellung.setBestellzeitpunkt(LocalDateTime.now());
            bestellung.setUpdatedAt(LocalDateTime.now());
            bestellung.setZahlungsart("PayPal");
            bestellung.setGesamtpreis(gesamtpreis);
            kunde.setVorgemerkt(kunde.getVorgemerkt().add(gesamtpreis));
            kunde.addBestellung(bestellung);
            warenkorbItems.clear();
            entityManager.persist(kunde);
            entityManager.flush();
            BestellungApi bestellungApi = BestellungMapper.mapToBestellungApi(bestellung);
            this.restaurantService.notifyRestaurant(restaurant.getEmail(), bestellungApi);
        }, () -> {
            throw new ResourceNotFoundException(
                    "Kunde nicht gefunden. Bitte melden Sie sich an, um eine Bestellung aufzugeben.");
        });
    }

    private List<BestellungItem> mapToBestellungItems(Bestellung bestellung, List<WarenkorbItem> warenkorbItems) {
        return warenkorbItems.stream().map(warenkorbItem -> {
            BestellungItem bestellungItem = new BestellungItem();
            bestellungItem.setItemLabel(warenkorbItem.getItem().getName());
            bestellungItem.setQuantity(warenkorbItem.getQuantity());
            bestellungItem.setPreis(warenkorbItem.getItem().getPreis());
            bestellungItem.setTotalPreis(warenkorbItem.getItem().getPreis() *
                    warenkorbItem.getQuantity());
            bestellungItem.setItem(warenkorbItem.getItem());
            bestellungItem.setBestellung(bestellung);
            return bestellungItem;
        }).collect(Collectors.toList());
    }

    public List<Bestellung> getBestellungen() {
        return this.kundeService.getCurrentAccount()
                .map(Kunde::getBestellungen)
                .orElse(Collections.emptyList());
    }

    public List<Bestellung> getRestaurantBestellungen() {
        return this.restaurantService.getCurrentAccount()
                .map(Restaurant::getBestellungen)
                .orElse(Collections.emptyList());
    }

    @Transactional
    public void confirmBestellung(Long bestellungId) {
        this.restaurantService.getCurrentAccount()
                .ifPresentOrElse(res -> {
                    Bestellung bestellung = this.findBestellungByIdAndStatus(res, bestellungId,
                            BestellStatus.BEARBEITUNG);
                    bestellung.setStatus(BestellStatus.ZUBEREITUNG);
                    bestellung.setUpdatedAt(LocalDateTime.now());
                    Kunde kunde = bestellung.getKunde();
                    kunde.setVorgemerkt(kunde.getVorgemerkt().subtract(bestellung.getGesamtpreis()));
                    kunde.setGeldbeutel(kunde.getGeldbeutel().subtract(bestellung.getGesamtpreis()));
                    BigDecimal restaurantWin = bestellung.getGesamtpreis()
                            .multiply(BigDecimal.valueOf(0.85))
                            .setScale(2, RoundingMode.HALF_UP);
                    BigDecimal restaurantGeldbeutel = res.getGeldbeutel();
                    res.setGeldbeutel(restaurantGeldbeutel.add(restaurantWin));
                }, () -> {
                    throw new ResourceNotFoundException(
                            "Restaurant nicht gefunden. Bitte melden Sie sich an, um Bestellungen zu bestätigen.");
                });
    }

    @Transactional
    public void stornierenBestellung(Long bestellungId) {
        this.restaurantService.getCurrentAccount()
                .ifPresentOrElse(res -> {
                    Bestellung bestellung = this.findBestellungByIdAndStatus(res, bestellungId,
                            BestellStatus.BEARBEITUNG);
                    bestellung.setStatus(BestellStatus.STORNIERT);
                    bestellung.setUpdatedAt(LocalDateTime.now());
                    Kunde kunde = bestellung.getKunde();
                    kunde.setVorgemerkt(kunde.getVorgemerkt().subtract(bestellung.getGesamtpreis()));
                }, () -> {
                    throw new ResourceNotFoundException(
                            "Restaurant nicht gefunden. Bitte melden Sie sich an, um Bestellungen zu stornieren.");
                });
    }

    @Transactional
    public void completeBestellung(Long bestellungId) {
        this.restaurantService.getCurrentAccount()
                .ifPresentOrElse(res -> {
                    Bestellung bestellung = this.findBestellungByIdAndStatus(res, bestellungId,
                            BestellStatus.ZUBEREITUNG);
                    bestellung.setStatus(BestellStatus.ABGESCHLOSSEN);
                    bestellung.setUpdatedAt(LocalDateTime.now());
                }, () -> {
                    throw new ResourceNotFoundException(
                            "Restaurant nicht gefunden. Bitte melden Sie sich an, um Bestellungen abzuschließen.");
                });
    }

    private Bestellung findBestellungByIdAndStatus(Restaurant res, Long bestellungId, BestellStatus statusCondition) {
        Bestellung bestellung = res.getBestellungen().stream()
                .filter(b -> b.getId().equals(bestellungId))
                .findFirst()
                .orElseThrow(
                        () -> new ResourceNotFoundException("Bestellung mit ID " + bestellungId + " nicht gefunden."));
        if (!statusCondition.equals(bestellung.getStatus())) {
            throw new ResourceException(
                    "Die Bestellung befindet sich nicht im erwarteten Status. Aktueller Status: "
                            + bestellung.getStatus());
        }
        return bestellung;
    }

    public Bestellung getBestellungenById(Long bestellungId) {
        return this.kundeService.getCurrentAccount()
                .map(kunde -> kunde.getBestellungen().stream()
                        .filter(bestellung -> bestellung.getId().equals(bestellungId))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Bestellung mit ID " + bestellungId + " nicht gefunden.")))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Kunde nicht gefunden. Bitte melden Sie sich an, um Ihre Bestellungen einzusehen."));
    }
}
