package com.example.strikeball.repository;

import com.example.strikeball.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByNicknameContainingIgnoreCase(String nickname);
    boolean existsByEmail(String email);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);
}