package com.uni.lieferspatz.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.uni.lieferspatz.constants.RolesConstants;
import com.uni.lieferspatz.domain.Kunde;
import com.uni.lieferspatz.domain.Restaurant;
import com.uni.lieferspatz.repository.KundeRepository;
import com.uni.lieferspatz.repository.RestaurantRepository;

@Transactional
public class AccountService implements UserDetailsService {

    private final KundeRepository kundeRepository;
    private final RestaurantRepository restaurantRepository;

    public AccountService(KundeRepository kundeRepository, RestaurantRepository restaurantRepository) {
        this.kundeRepository = kundeRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String lowerEmail = email.toLowerCase();
        Kunde kunde = this.kundeRepository.findByEmail(lowerEmail).orElse(null);
        if (kunde != null) {
            return new User(kunde.getEmail(), kunde.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority(RolesConstants.KUNDE)));
        }
        Restaurant restaurant = this.restaurantRepository.findByEmail(lowerEmail).orElse(null);
        if (restaurant != null) {
            return new User(restaurant.getEmail(), restaurant.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority(RolesConstants.RESTAURANT)));
        }
        throw new UsernameNotFoundException("User existiert nicht");
    }

}
