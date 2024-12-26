package com.uni.lieferspatz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uni.lieferspatz.domain.LieferPlz;

@Repository
public interface LieferPlzRepository extends JpaRepository<LieferPlz, Long> {

    void deleteByRestaurantId(Long restaurantId);

}
