package com.example.strikeball.repository;

import com.example.strikeball.model.WeaponType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeaponTypeRepository extends JpaRepository<WeaponType, Long> {
    List<WeaponType> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
}