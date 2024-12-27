package com.uni.lieferspatz.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WarenkorbKey implements Serializable {
    public static final long serialVersionUID = 1L;
    @Column(name = "kunde_id", nullable = false)
    private Long kundeId;
    @Column(name = "item_id", nullable = false)
    private Long itemId;
}
