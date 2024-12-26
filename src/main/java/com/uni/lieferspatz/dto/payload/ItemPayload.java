package com.uni.lieferspatz.dto.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPayload {
    private Long id;
    private String name;
    private String beschreibung;
    private double preis;
    private String imageUrl;
}
