package com.uni.lieferspatz.dto.payload;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpeningHoursPayload {
    private Long id;
    private Long restaurantId;
    private String dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
}
