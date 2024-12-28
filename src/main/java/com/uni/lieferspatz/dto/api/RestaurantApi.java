package com.uni.lieferspatz.dto.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantApi extends UserApi {
	private String email;
	private String name;
	private String beschreibung;
	private String image;
	private double geldbeutel;
}
