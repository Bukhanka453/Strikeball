package com.example.strikeball;

import com.example.strikeball.model.Role;
import com.example.strikeball.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StrikeballApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrikeballApplication.class, args);
    }

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                roleRepository.save(new Role("ROLE_ADMIN", "Полный доступ ко всему"));
            }
            if (roleRepository.findByName("ROLE_ORGANIZER").isEmpty()) {
                roleRepository.save(new Role("ROLE_ORGANIZER", "Может организовывать игры"));
            }
            if (roleRepository.findByName("ROLE_PLAYER").isEmpty()) {
                roleRepository.save(new Role("ROLE_PLAYER", "Обычный игрок, только просмотр"));
            }
        };
    }
}