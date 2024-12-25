package com.uni.lieferspatz.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPayload {
    private String strasse;
    private String plz;
    private String ort;
    private String password;
    private String email;
}
