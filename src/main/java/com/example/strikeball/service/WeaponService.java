package com.example.strikeball.service;

import com.example.strikeball.model.Weapon;
import com.example.strikeball.repository.WeaponRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeaponService {

    private final WeaponRepository weaponRepository;

    public WeaponService(WeaponRepository weaponRepository) {
        this.weaponRepository = weaponRepository;
    }

    public List<Weapon> findAll() {
        return weaponRepository.findAll();
    }

    public Optional<Weapon> findById(Long id) {
        return weaponRepository.findById(id);
    }

    public List<Weapon> searchByName(String name) {
        if (name == null || name.isBlank()) return weaponRepository.findAll();
        return weaponRepository.findByNameContainingIgnoreCase(name);
    }

    public Weapon save(Weapon weapon) {
        return weaponRepository.save(weapon);
    }

    public void deleteById(Long id) {
        weaponRepository.deleteById(id);
    }
}