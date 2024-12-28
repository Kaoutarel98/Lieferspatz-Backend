package com.uni.lieferspatz.domain;

import java.util.List;
import java.util.Set;

import com.uni.lieferspatz.constants.RolesConstants;

import jakarta.persistence.CascadeType;
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
    @OneToMany(mappedBy = "restaurant")
    private List<OpeningHours> openingHours;
    @OneToMany(mappedBy = "restaurant")
    private List<LieferPlz> lieferPlz;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bestellung> bestellungen;

    @Override
    public String getRole() {
        return RolesConstants.RESTAURANT;
    }
}
