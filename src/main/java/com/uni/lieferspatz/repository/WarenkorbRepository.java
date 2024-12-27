package com.uni.lieferspatz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uni.lieferspatz.domain.WarenkorbItem;
import com.uni.lieferspatz.domain.WarenkorbKey;

@Repository
public interface WarenkorbRepository extends JpaRepository<WarenkorbItem, WarenkorbKey> {
}
