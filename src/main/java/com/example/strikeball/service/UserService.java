package com.example.strikeball.service;

import com.example.strikeball.model.User;
import com.example.strikeball.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> searchByNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            return userRepository.findAll();
        }
        return userRepository.findByNicknameContainingIgnoreCase(nickname);
    }

    public User save(User user) {
        // Хэшируем пароль, если он ещё не захэширован
        if (user.getPassword() != null && !user.getPassword().startsWith("$2")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}