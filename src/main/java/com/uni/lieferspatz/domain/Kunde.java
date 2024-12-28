package com.uni.lieferspatz.domain;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
    @Column(name = "vorgemerkt", precision = 15, scale = 2)
    @NotNull
    private BigDecimal vorgemerkt = BigDecimal.ZERO;
    @OneToMany(mappedBy = "kunde", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarenkorbItem> warenkorbItems;
    @OneToMany(mappedBy = "kunde", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bestellung> bestellungen;
}
