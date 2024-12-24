package com.uni.lieferspatz.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.uni.lieferspatz.domain.Kunde;
import com.uni.lieferspatz.repository.KundeRepository;

@Transactional
public class AccountService implements UserDetailsService {

    private final KundeRepository kundeRepository;

    public AccountService(KundeRepository kundeRepository) {
        this.kundeRepository = kundeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String lowerEmail = email.toLowerCase();
        Optional<Kunde> kundeOptional = this.kundeRepository.findByEmail(lowerEmail);
        if (kundeOptional.isPresent()) {
            Kunde kunde = kundeOptional.get();
            return new User(kunde.getEmail(), kunde.getPassword(), Collections.emptyList());
        } else {
            throw new UsernameNotFoundException("User existiert nicht");
        }
    }

}
