package com.uni.lieferspatz.dto.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailableRestaurantApi extends UserApi {
    private String name;
    private String beschreibung;
    private String image;
}
