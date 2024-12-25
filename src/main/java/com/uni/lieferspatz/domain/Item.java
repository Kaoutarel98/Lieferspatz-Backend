package com.uni.lieferspatz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item")
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "beschreibung")
    @NotNull
    private String beschreibung;

    @Column(name = "preis")
    @NotNull
    private double preis;

    @Column(name = "image_url")
    private String imageUrl;

    @JoinColumn(name = "restaurant_id", nullable = false)
    @ManyToOne
    private Restaurant restaurant;
}
