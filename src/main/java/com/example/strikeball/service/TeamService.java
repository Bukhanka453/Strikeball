package com.example.strikeball.service;

import com.example.strikeball.model.Team;
import com.example.strikeball.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
    }

    public List<Team> searchByName(String name) {
        if (name == null || name.isBlank()) {
            return teamRepository.findAll();
        }
        return teamRepository.findByNameContainingIgnoreCase(name);
    }

    public Team save(Team team) {
        return teamRepository.save(team);
    }

    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }
}