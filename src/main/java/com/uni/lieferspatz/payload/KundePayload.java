package com.uni.lieferspatz.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KundePayload {
    private String vorname;
    private String nachname;
    private String strasse;
    private String plz;
    private String ort;
    private String password;
    private String email;
}
