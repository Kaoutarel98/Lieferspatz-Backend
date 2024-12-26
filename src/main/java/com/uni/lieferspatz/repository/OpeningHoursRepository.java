package com.uni.lieferspatz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uni.lieferspatz.domain.OpeningHours;

@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHours, Long> {
    public List<OpeningHours> findAllByIdIn(List<Long> ids);
}
