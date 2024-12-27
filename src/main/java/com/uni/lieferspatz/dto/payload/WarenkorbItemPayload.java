package com.uni.lieferspatz.dto.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarenkorbItemPayload {
    private Long itemId;
    private int quantity;
}
