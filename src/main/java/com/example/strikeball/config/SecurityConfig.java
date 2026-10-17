package com.example.strikeball.config;

import com.example.strikeball.security.JwtAuthenticationFilter;
import com.example.strikeball.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService,JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(auth -> auth
                        // Публичные страницы
                        .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        // Swagger UI
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        // REST API — публичный логин
                        .requestMatchers("/api/auth/**").permitAll()
                        // Остальной REST API — только аутентифицированные
                        .requestMatchers("/api/**").authenticated()
                        // Только ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        // ADMIN и ORGANIZER
                        .requestMatchers("/games/new", "/games/*/edit", "/games/*/delete").hasAnyRole("ADMIN", "ORGANIZER")
                        .requestMatchers("/teams/new", "/teams/*/edit", "/teams/*/delete").hasAnyRole("ADMIN", "ORGANIZER")
                        .requestMatchers("/locations/new", "/locations/*/edit", "/locations/*/delete").hasAnyRole("ADMIN", "ORGANIZER")
                        // Всё остальное
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}