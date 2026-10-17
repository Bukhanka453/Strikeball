package com.example.strikeball.rest;

import com.example.strikeball.exception.ResourceNotFoundException;
import com.example.strikeball.model.User;
import com.example.strikeball.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Пользователи", description = "Операции с пользователями (CRUD)")
@SecurityRequirement(name = "bearerAuth")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Получить список пользователей", description = "Возвращает список пользователей с фильтром по никнейму.")
    @ApiResponse(responseCode = "200", description = "Список получен")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@Parameter(description = "Поиск по никнейму", example = "Admin") @RequestParam(required = false) String search) {
        return ResponseEntity.ok(userService.searchByNickname(search));
    }

    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@Parameter(description = "ID пользователя", example = "1") @PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"));
    }

    @Operation(summary = "Создать пользователя", description = "Пароль автоматически хэшируется BCrypt. "
            + "Валидация: никнейм от 3 до 30 символов, email корректный, " + "пароль от 6 до 100 символов, возраст от 12 до 100.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @Operation(summary = "Обновить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@Parameter(description = "ID пользователя", example = "1") @PathVariable Long id, @Valid @RequestBody User user) {
        userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"));
        user.setId(id);
        return ResponseEntity.ok(userService.save(user));
    }

    @Operation(summary = "Удалить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Parameter(description = "ID пользователя", example = "1") @PathVariable Long id) {
        userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"));
        userService.deleteById(id);
    }
}