package com.uni.lieferspatz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "kunde")
@Getter
@Setter
public class Kunde extends User {

    @Column(name = "vorname")
    private String vorname;

    @Column(name = "nachname")
    private String nachname;

}
