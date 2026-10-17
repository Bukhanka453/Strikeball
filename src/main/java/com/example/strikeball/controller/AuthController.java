package com.example.strikeball.controller;

import com.example.strikeball.model.Role;
import com.example.strikeball.model.User;
import com.example.strikeball.repository.RoleRepository;
import com.example.strikeball.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (userRepository.findByNickname(user.getNickname()).isPresent()) {
            model.addAttribute("nicknameError", "Такой никнейм уже занят");
            return "register";
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("emailError", "Такой email уже зарегистрирован");
            return "register";
        }

        // Хэшируем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Выдаём роль PLAYER по умолчанию
        Role playerRole = roleRepository.findByName("ROLE_PLAYER")
                .orElseThrow(() -> new IllegalStateException("Роль ROLE_PLAYER не найдена в БД"));
        user.setRoles(Set.of(playerRole));

        userRepository.save(user);
        return "redirect:/login?registered=true";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}