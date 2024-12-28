package com.uni.lieferspatz.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "strasse")
    @NotNull
    private String strasse;
    @Column(name = "plz")
    @NotNull
    private String plz;
    @Column(name = "ort")
    @NotNull
    private String ort;
    @Email
    @NotNull
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password_hash", nullable = false)
    @JsonIgnore
    @NotNull
    private String password;
    @Column(name = "geldbeutel", precision = 15, scale = 2)
    @NotNull
    private BigDecimal geldbeutel;

    public abstract String getRole();
}
