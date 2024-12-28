package com.uni.lieferspatz.domain;

import java.util.Map;

import com.uni.lieferspatz.domain.converter.JsonConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bestellung_item")
@Getter
@Setter
public class BestellungItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "bestellung_id", nullable = false)
    private Bestellung bestellung;
    @Column(name = "item_label")
    private String itemLabel;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "preis")
    private double preis;
    @Column(name = "total_preis")
    private double totalPreis;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @Column(name = "bestellung_data", columnDefinition = "TEXT")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> bestellungData;

    @PrePersist
    public void prePersist() {
        if (this.bestellungData == null) {
            this.bestellungData = Map.of(//
                    "itemName", this.item.getName(), //
                    "itemBeschreibung", this.item.getBeschreibung(), //
                    "itemPreis", this.item.getPreis(), //
                    "itemImageUrl", this.item.getImageUrl() != null ? this.item.getImageUrl() : "");
        }
    }
}
