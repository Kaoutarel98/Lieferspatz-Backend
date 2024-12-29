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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.uni.lieferspatz.repository.KundeRepository;
import com.uni.lieferspatz.repository.RestaurantRepository;
import com.uni.lieferspatz.service.AccountService;
import com.uni.lieferspatz.service.auth.JwtFilter;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;

    public SecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }

    @Bean
    public UserDetailsService accountDetailsService(KundeRepository kundeRepository,
            RestaurantRepository restaurantRepository) {
        return new AccountService(kundeRepository, restaurantRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //@formatter:off
        http
        .csrf(csrf -> csrf.disable())
        .addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)
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
            .requestMatchers(AntPathRequestMatcher.antMatcher("/app/**/*.{js,html}")).permitAll() // Allow .js files in /app (recursive)
            .requestMatchers("/i18n/**").permitAll() // Allow internationalization files
            .requestMatchers("/content/**").permitAll() // Allow content resources
            .requestMatchers("/swagger-ui/**").permitAll() // Allow Swagger UI
            .requestMatchers("/test/**").permitAll()
            .requestMatchers("/order-request/**").hasRole("RESTAURANT")
            .requestMatchers("/api/**/erstellen").permitAll()
            .requestMatchers("/api/**/login").permitAll()
            .requestMatchers("/api/**/kunde").hasRole("KUNDE")
            .requestMatchers("/api/**/warenkorb").hasRole("KUNDE")
            .requestMatchers("/api/**").authenticated()
        )
        .httpBasic(httpBasic -> {}); // Enable Basic Authentication
    //@formatter:on 
        return http.build();
    }
}
