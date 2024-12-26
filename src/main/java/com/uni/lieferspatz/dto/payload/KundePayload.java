package com.uni.lieferspatz.dto.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KundePayload extends UserPayload {
    private String vorname;
    private String nachname;
}
