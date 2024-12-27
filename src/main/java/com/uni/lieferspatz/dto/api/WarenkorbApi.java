package com.uni.lieferspatz.dto.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarenkorbApi {
    private Long itemId;
    private double preis;
    private int quantity;

}
