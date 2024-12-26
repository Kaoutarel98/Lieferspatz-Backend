package com.uni.lieferspatz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uni.lieferspatz.domain.OpeningHours;

@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHours, Long> {

}
