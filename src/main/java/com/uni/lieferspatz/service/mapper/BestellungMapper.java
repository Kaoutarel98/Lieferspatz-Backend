package com.uni.lieferspatz.service.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.uni.lieferspatz.constants.BestellStatus;
import com.uni.lieferspatz.domain.Bestellung;
import com.uni.lieferspatz.domain.BestellungItem;
import com.uni.lieferspatz.dto.api.BestellungApi;
import com.uni.lieferspatz.dto.api.BestellungItemApi;

public class BestellungMapper {
	public static BestellungApi mapToBestellungApi(Bestellung bestellung) {
		BestellungApi bestellungApi = new BestellungApi();
		bestellungApi.setId(bestellung.getId());
		bestellungApi.setRestaurantName(bestellung.getRestaurant().getName());
		bestellungApi.setKundeName(bestellung.getKunde().getVorname() + " " + bestellung.getKunde().getNachname());
		bestellungApi.setGesamtpreis(bestellung.getGesamtpreis().doubleValue());
		bestellungApi.setStatus(bestellung.getStatus().name());
		bestellungApi.setLieferAdresse(bestellung.getLieferAdresse());
		bestellungApi.setBestellzeitpunkt(bestellung.getBestellzeitpunkt());
		bestellungApi.setZahlungsart(bestellung.getZahlungsart());
		List<BestellungItemApi> bestellungItemApis = new ArrayList<>();
		for (BestellungItem bestellungItem : bestellung.getBestellungItems()) {
			BestellungItemApi bestellungItemApi = new BestellungItemApi();
			bestellungItemApi.setItemLabel(bestellungItem.getItemLabel());
			bestellungItemApi.setQuantity(bestellungItem.getQuantity());
			bestellungItemApi.setPreis(bestellungItem.getPreis());
			bestellungItemApi.setTotalPreis(bestellungItem.getTotalPreis());
			bestellungItemApis.add(bestellungItemApi);
		}
		bestellungApi.setBestellungItems(bestellungItemApis);
		return bestellungApi;
	}

	public static List<BestellungApi> mapToBestellungApi(List<Bestellung> bestellungen) {
		return bestellungen.stream()
				.map(BestellungMapper::mapToBestellungApi)
				.sorted((a, b) -> {
					int statusComparison = Integer.compare(BestellStatus.getPriority(a.getStatus()),
							BestellStatus.getPriority(b.getStatus()));
					if (statusComparison != 0) {
						return statusComparison;
					}
					return b.getBestellzeitpunkt().compareTo(a.getBestellzeitpunkt());
				})
				.collect(Collectors.toList());
	}
}
