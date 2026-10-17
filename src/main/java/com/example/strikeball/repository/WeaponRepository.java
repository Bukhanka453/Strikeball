package com.example.strikeball.repository;

import com.example.strikeball.model.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeaponRepository extends JpaRepository<Weapon, Long> {
    List<Weapon> findByNameContainingIgnoreCase(String name);
}