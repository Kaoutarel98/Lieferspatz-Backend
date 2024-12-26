package com.uni.lieferspatz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uni.lieferspatz.domain.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    public Optional<Restaurant> findByEmail(String email);

    public Optional<Restaurant> findOneByEmailIgnoreCase(String email);

}
