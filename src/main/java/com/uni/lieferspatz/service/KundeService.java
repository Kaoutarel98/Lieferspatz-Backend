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

import com.uni.lieferspatz.domain.Item;
import com.uni.lieferspatz.domain.Kunde;
import com.uni.lieferspatz.domain.LieferPlz;
import com.uni.lieferspatz.domain.OpeningHours;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.repository.KundeRepository;
import com.uni.lieferspatz.repository.LieferPlzRepository;
import com.uni.lieferspatz.service.auth.SecurityUtils;

@Service
public class KundeService {
    private final PasswordEncoder passwordEncoder;
    private final KundeRepository kundeRepository;
    private final LieferPlzRepository lieferPlzRepository;
    private final RestaurantService restaurantService;

    public KundeService(PasswordEncoder passwordEncoder, KundeRepository kundeRepository,
            LieferPlzRepository lieferPlzRepository,
            RestaurantService restaurantService) {
        this.kundeRepository = kundeRepository;
        this.passwordEncoder = passwordEncoder;
        this.lieferPlzRepository = lieferPlzRepository;
        this.restaurantService = restaurantService;
    }

    public void saveNeueKunde(Kunde kunde) {
        String encodedPassword = passwordEncoder.encode(kunde.getPassword());
        kunde.setPassword(encodedPassword);
        this.kundeRepository.save(kunde);
    }

    public List<Restaurant> getRelatedRestaurant() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime();
        // getCurrentAccount():
        // نقرأ البريد الالكتروني للحساب الحالي تم نقرأ معلومات الحساب من قاعدة البيانات
        // اذا لم يكن الحساب موجود نعيد قائمة فارغة
        // Optional<Kunde>
        List<LieferPlz> relatedPlz = this.getCurrentAccount()//
                .map(Kunde::getPlz)//
                .map(plz -> this.lieferPlzRepository.findAllByPlz(plz))
                .orElse(Collections.emptyList());
        return relatedPlz.stream()//
                .map(lieferPlz -> lieferPlz.getRestaurant())
                .map(Restaurant::getOpeningHours)
                .flatMap(List::stream)
                .filter(openingHours -> this.isRestaurantOpen(openingHours, currentTime, today))
                .map(openingHours -> openingHours.getRestaurant())//
                .collect(Collectors.toList());
    }

    public List<Item> getRestaurantItems(Long restaurantId) {
        return this.restaurantService.getRestaurant(restaurantId)//
                .getItems().stream()//
                .collect(Collectors.toList());
    }

    private boolean isRestaurantOpen(OpeningHours openingHours, LocalTime currenTime, DayOfWeek today) {
        return openingHours.getDayOfWeek().equals(today)//
                && openingHours.getOpenTime() != null//
                && openingHours.getCloseTime() != null//
                && currenTime.isAfter(openingHours.getOpenTime())//
                && currenTime.isBefore(openingHours.getCloseTime());
        // today == openingHours.getDayOfWeek()
        // currentTime zwischen öffungszeit und schließzeit -> currentTime nach
        // öffungszeit und vor schließzeit
        // Montag um 12:00 -> 00:00
    }

    public Optional<Kunde> getCurrentAccount() {
        // نحصل على الحساب الحالي من خلال البريد الإلكتروني
        return SecurityUtils.getCurrentAccountEmail().flatMap(this.kundeRepository::findOneByEmailIgnoreCase);
    }
}
