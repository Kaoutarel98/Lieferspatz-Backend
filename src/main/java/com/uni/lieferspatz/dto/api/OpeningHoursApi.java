package com.uni.lieferspatz.dto.api;

import java.time.DayOfWeek;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpeningHoursApi {
	private Long id;
	private Long restaurantId;
	private DayOfWeek dayOfWeek;
	private String openTime;
	private String closeTime;
}
