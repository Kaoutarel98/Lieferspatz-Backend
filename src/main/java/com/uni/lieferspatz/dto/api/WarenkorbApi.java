package com.uni.lieferspatz.dto.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarenkorbApi {
    private Long itemId;
    private String name;
    private double preis;
    private String beschreibung;
    private int quantity;
    private String remark;
}
