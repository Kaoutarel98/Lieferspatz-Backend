package com.uni.lieferspatz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uni.lieferspatz.domain.Kunde;

@Repository
public interface KundeRepository extends JpaRepository<Kunde, Long> {
    // SELECT * FROM kunde WHERE email = ?
    public Optional<Kunde> findByEmail(String email);

    // SELECT * FROM kunde WHERE UPPER(email) = UPPER(?)
    public Optional<Kunde> findOneByEmailIgnoreCase(String email);
}
