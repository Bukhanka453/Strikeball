package com.example.strikeball.service;

import com.example.strikeball.model.Role;
import com.example.strikeball.model.User;
import com.example.strikeball.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nicknameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(nicknameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(nicknameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                "Пользователь не найден: " + nicknameOrEmail)));

        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getNickname(), user.getPassword(), authorities);
    }
}