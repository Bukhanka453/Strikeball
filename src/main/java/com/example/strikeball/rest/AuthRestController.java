package com.example.strikeball.rest;

import com.example.strikeball.security.JwtService;
import com.example.strikeball.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация", description = "Эндпоинты для входа и получения JWT-токена")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthRestController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Вход в систему",
            description = "Принимает логин и пароль, возвращает JWT-токен для авторизации в Swagger.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход, токен получен", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль", content = @Content),
            @ApiResponse(responseCode = "400", description = "Некорректные данные", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @Schema(description = "Данные для входа в систему")
    public record LoginRequest(@NotBlank(message = "Никнейм обязателен") @Schema(description = "Никнейм или email", example = "AdminUser") String username,
                               @NotBlank(message = "Пароль обязателен") @Schema(description = "Пароль", example = "password123") String password) {}
}