package com.uni.lieferspatz.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.lieferspatz.models.Kunde;

//localhost:8080/api/v1/kunde/erstellen
@RestController
@RequestMapping(value = "/api/v1/kunde")
public class KundeController {

    @PostMapping("/erstellen")
    public String kundeErstellen(@RequestBody Kunde kunde) {
        System.out.println(kunde.getVorname());
        System.out.println(kunde.getNachname());
        System.out.println(kunde.getEmail());
        System.out.println(kunde.getPassword());
        return "";
    }
    
}
