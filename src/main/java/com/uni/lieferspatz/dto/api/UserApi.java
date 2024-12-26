package com.uni.lieferspatz.dto.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserApi {
    private Long id;
    private String strasse;
    private String plz;
    private String ort;
}
