package com.uni.lieferspatz.dto.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantPayload extends UserPayload {
    private String name;
    private String beschreibung;
    private String image;

}
