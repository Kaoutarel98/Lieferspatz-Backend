package com.uni.lieferspatz.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uni.lieferspatz.domain.Kunde;
import com.uni.lieferspatz.domain.LieferPlz;
import com.uni.lieferspatz.domain.OpeningHours;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.domain.User;
import com.uni.lieferspatz.dto.payload.KundePayload;
import com.uni.lieferspatz.repository.KundeRepository;
import com.uni.lieferspatz.repository.LieferPlzRepository;
import com.uni.lieferspatz.service.auth.SecurityUtils;
import com.uni.lieferspatz.service.mapper.UserMapper;

@Service
public class KundeService {

    private final PasswordEncoder passwordEncoder;
    private final KundeRepository kundeRepository;
    private final LieferPlzRepository lieferPlzRepository;

    public KundeService(PasswordEncoder passwordEncoder, KundeRepository kundeRepository,
            LieferPlzRepository lieferPlzRepository) {
        this.kundeRepository = kundeRepository;
        this.passwordEncoder = passwordEncoder;
        this.lieferPlzRepository = lieferPlzRepository;
    }

    public void saveNeueKunde(KundePayload kundePayload) {
        Kunde newKunde = UserMapper.mapKunde(kundePayload, passwordEncoder);
        this.kundeRepository.save(newKunde);
    }

    public List<Restaurant> getRelatedRestaurant() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime();
        List<LieferPlz> relatedPlz = this.getCurrentAccount()//
                .map(user -> this.lieferPlzRepository.findAllByPlz(user.getPlz()))
                .orElse(Collections.emptyList());
        return relatedPlz.stream()//
                .map(LieferPlz::getRestaurant)
                .map(Restaurant::getOpeningHours)
                .flatMap(List::stream)
                .filter(openingHours -> this.isRestaurantOpen(openingHours, currentTime, today))
                .map(openingHours -> openingHours.getRestaurant())//
                .collect(Collectors.toList());
    }

    private boolean isRestaurantOpen(OpeningHours openingHours, LocalTime currenTime, DayOfWeek today) {
        return openingHours.getDayOfWeek().equals(today)//
                && currenTime.isAfter(openingHours.getOpenTime())//
                && currenTime.isBefore(openingHours.getCloseTime());
    }

    public Optional<User> getCurrentAccount() {
        return SecurityUtils.getCurrentAccountEmail().flatMap(this.kundeRepository::findOneByEmailIgnoreCase);
    }
}
