package com.uni.lieferspatz.domain;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
public class Restaurant extends User {

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "beschreibung")
    @NotNull
    private String beschreibung;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "restaurant")
    private Set<Item> items;
}
