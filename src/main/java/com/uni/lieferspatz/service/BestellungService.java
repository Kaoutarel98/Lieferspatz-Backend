package com.uni.lieferspatz.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uni.lieferspatz.domain.Bestellung;
import com.uni.lieferspatz.domain.BestellungItem;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.domain.WarenkorbItem;

@Service
public class BestellungService {
    private final KundeService kundeService;

    public BestellungService(KundeService kundeService) {
        this.kundeService = kundeService;
    }

    @Transactional
    public void saveBestellung() {
        this.kundeService.getCurrentAccount().ifPresent(kunde -> {
            List<WarenkorbItem> warenkorbItems = kunde.getWarenkorbItems();
            if (warenkorbItems.size() == 0) {
                throw new RuntimeException("Warenkorb ist leer");
            }
            Bestellung bestellung = new Bestellung();
            Restaurant restaurant = warenkorbItems.get(0).getItem().getRestaurant();
            bestellung.setKunde(kunde);
            bestellung.setRestaurant(restaurant);
            bestellung.setStatus("Bestellt");
            bestellung.setLieferAdresse(String.format("%s, %s %s", kunde.getStrasse(),
                    kunde.getPlz(), kunde.getOrt()));
            bestellung.setBestellzeitpunkt(LocalDateTime.now());
            bestellung.setUpdatedAt(LocalDateTime.now());
            bestellung.setZahlungsart("PayPal");
            List<BestellungItem> bestellungItems = this.mapToBestellungItems(bestellung, warenkorbItems);
            bestellung.setBestellungItems(bestellungItems);
            double gesamtpreis = bestellungItems.stream()
                    .mapToDouble(BestellungItem::getTotalPreis)
                    .sum();
            bestellung.setGesamtpreis(gesamtpreis);
            kunde.getBestellungen().add(bestellung);
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
}
