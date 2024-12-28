package com.uni.lieferspatz.dto.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KundeApi extends UserApi {
	private String email;
	private String vorname;
	private String nachname;
	private double geldbeutel;
	private double vorgemerkt;
}
