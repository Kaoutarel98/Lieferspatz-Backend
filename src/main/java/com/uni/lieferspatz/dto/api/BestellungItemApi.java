package com.uni.lieferspatz.dto.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BestellungItemApi {
	private String itemLabel;
	private int quantity;
	private double preis;
	private double totalPreis;
}
