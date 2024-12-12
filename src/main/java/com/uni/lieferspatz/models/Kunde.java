package com.uni.lieferspatz.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Kunde {
    private String vorname;
    private String nachname;
    private String strasse;
    private String plz;
    private String ort;
    private String password;
    private String email;
}
