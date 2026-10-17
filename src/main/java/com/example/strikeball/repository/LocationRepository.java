package com.example.strikeball.repository;

import com.example.strikeball.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByNameContainingIgnoreCase(String name);
}