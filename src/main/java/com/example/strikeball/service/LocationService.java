package com.example.strikeball.service;

import com.example.strikeball.model.Location;
import com.example.strikeball.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> searchByName(String name) {
        if (name == null || name.isBlank()) {
            return locationRepository.findAll();
        }
        return locationRepository.findByNameContainingIgnoreCase(name);
    }

    public Location save(Location location) {
        return locationRepository.save(location);
    }

    public void deleteById(Long id) {
        locationRepository.deleteById(id);
    }
}