package com.uni.lieferspatz.dto.payload;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpeningHoursPayload {
    private Long id;
    private Long restaurantId;
    private DayOfWeek dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;

}
