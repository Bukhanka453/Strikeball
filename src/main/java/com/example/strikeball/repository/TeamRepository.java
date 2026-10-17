package com.example.strikeball.repository;

import com.example.strikeball.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByNameContainingIgnoreCase(String name);
}