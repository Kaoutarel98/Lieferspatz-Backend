package com.uni.lieferspatz.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "warenkorb")
@Getter
@Setter
public class WarenkorbItem {
    @EmbeddedId
    private WarenkorbKey id;
    @JoinColumn(name = "kunde_id", nullable = false)
    @ManyToOne
    @MapsId("kundeId")
    private Kunde kunde;
    @JoinColumn(name = "item_id", nullable = false)
    @ManyToOne
    @MapsId("itemId")
    private Item item;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "added_at")
    private LocalDateTime addedAt;
}
