package com.uni.lieferspatz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
public class Restaurant extends User {

    @Column(name = "name")
    private String name;
    @Column(name = "beschreibung")
    private String beschreibung;
    @Column(name = "image")
    private String image;
}
