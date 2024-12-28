package com.uni.lieferspatz.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.uni.lieferspatz.constants.BestellStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Table(name = "bestellung")
@Entity
@Getter
@Setter
public class Bestellung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    @ManyToOne
    @JoinColumn(name = "kunde_id", nullable = false)
    private Kunde kunde;
    @Column(name = "gesamtpreis", precision = 15, scale = 2)
    @NotNull
    private BigDecimal gesamtpreis;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BestellStatus status;
    @Column(name = "lieferadresse")
    private String lieferAdresse;
    @Column(name = "bestellzeitpunkt")
    private LocalDateTime bestellzeitpunkt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "zahlungsart")
    private String zahlungsart;
    @OneToMany(mappedBy = "bestellung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BestellungItem> bestellungItems;
}
