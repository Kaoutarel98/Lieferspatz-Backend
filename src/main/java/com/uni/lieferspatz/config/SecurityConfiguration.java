package com.uni.lieferspatz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

import com.uni.lieferspatz.repository.KundeRepository;
import com.uni.lieferspatz.service.AccountService;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }

    @Bean
    public UserDetailsService accountDetailsService(KundeRepository kundeRepository) {
        return new AccountService(kundeRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //@formatter:off
        http
        .csrf(csrf -> csrf.disable())
        .headers(headers -> headers
            .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:")) 
            .frameOptions(frameOptions -> frameOptions.deny())
            .cacheControl(cache -> {}) // Enable Cache-Control headers
            .xssProtection(xss -> xss.disable()) // Enable XSS protection
            .referrerPolicy(referrer -> referrer
                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            )
            .permissionsPolicyHeader(permissions -> permissions
                .policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()")
            )
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow all OPTIONS requests
            .requestMatchers("/app/**/*.{js,html}").permitAll() // Allow .js files in /app (recursive)
            .requestMatchers("/i18n/**").permitAll() // Allow internationalization files
            .requestMatchers("/content/**").permitAll() // Allow content resources
            .requestMatchers("/swagger-ui/**").permitAll() // Allow Swagger UI
            .requestMatchers("/test/**").permitAll()
            .requestMatchers("/api/**").permitAll()
        )
        .httpBasic(httpBasic -> {}); // Enable Basic Authentication
    //@formatter:on 
        return http.build();
    }

}
