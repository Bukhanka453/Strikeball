package com.example.strikeball.service;

import com.example.strikeball.model.WeaponType;
import com.example.strikeball.repository.WeaponTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeaponTypeService {

    private final WeaponTypeRepository weaponTypeRepository;

    public WeaponTypeService(WeaponTypeRepository weaponTypeRepository) {
        this.weaponTypeRepository = weaponTypeRepository;
    }

    public List<WeaponType> findAll() {
        return weaponTypeRepository.findAll();
    }

    public Optional<WeaponType> findById(Long id) {
        return weaponTypeRepository.findById(id);
    }

    public List<WeaponType> searchByName(String name) {
        if (name == null || name.isBlank()) return weaponTypeRepository.findAll();
        return weaponTypeRepository.findByNameContainingIgnoreCase(name);
    }

    public WeaponType save(WeaponType weaponType) {
        return weaponTypeRepository.save(weaponType);
    }

    public void deleteById(Long id) {
        weaponTypeRepository.deleteById(id);
    }
}