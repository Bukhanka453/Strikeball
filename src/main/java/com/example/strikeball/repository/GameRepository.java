package com.example.strikeball.repository;

import com.example.strikeball.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByNameContainingIgnoreCase(String name);
}