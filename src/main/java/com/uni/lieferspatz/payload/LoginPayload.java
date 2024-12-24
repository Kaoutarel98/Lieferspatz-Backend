package com.uni.lieferspatz.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginPayload {
    private String email;
    private String password;

}
