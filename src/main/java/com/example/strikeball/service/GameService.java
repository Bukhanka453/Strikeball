package com.example.strikeball.service;

import com.example.strikeball.model.Game;
import com.example.strikeball.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Optional<Game> findById(Long id) {
        return gameRepository.findById(id);
    }

    public List<Game> searchByName(String name) {
        if (name == null || name.isBlank()) {
            return gameRepository.findAll();
        }
        return gameRepository.findByNameContainingIgnoreCase(name);
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public void deleteById(Long id) {
        gameRepository.deleteById(id);
    }
}