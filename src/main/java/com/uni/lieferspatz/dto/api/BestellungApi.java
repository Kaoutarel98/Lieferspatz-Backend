package com.uni.lieferspatz.dto.api;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BestellungApi {
	private Long id;
	private String restaurantName;
	private String kundeName;
	private double gesamtpreis;
	private String status;
	private String lieferAdresse;
	private LocalDateTime bestellzeitpunkt;
	private String zahlungsart;
	private List<BestellungItemApi> bestellungItems;
}
