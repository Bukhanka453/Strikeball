package com.example.strikeball.rest;

import com.example.strikeball.exception.ResourceNotFoundException;
import com.example.strikeball.model.Game;
import com.example.strikeball.service.GameService;
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
@RequestMapping("/api/games")
@Tag(name = "Игры", description = "Операции с играми (CRUD)")
@SecurityRequirement(name = "bearerAuth")
public class GameRestController {

    private final GameService gameService;

    public GameRestController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(summary = "Получить список всех игр", description = "Возвращает список игр. Можно отфильтровать по названию через параметр search.")
    @ApiResponse(responseCode = "200", description = "Список игр получен")
    @GetMapping
    public ResponseEntity<List<Game>> getAllGames(
            @Parameter(description = "Поиск по названию (частичное совпадение, без учёта регистра)", example = "лес")
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(gameService.searchByName(search));
    }

    @Operation(summary = "Получить игру по ID", description = "Возвращает одну игру по её идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Игра найдена"),
            @ApiResponse(responseCode = "404", description = "Игра не найдена", content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(
            @Parameter(description = "ID игры", example = "1")
            @PathVariable Long id) {
        return gameService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Игра с id " + id + " не найдена"));
    }

    @Operation(summary = "Создать новую игру",
            description = "Создаёт игру. Поля location и team должны содержать существующие ID. "
                    + "Валидация: название от 3 до 150 символов, дата не в прошлом, "
                    + "maxPlayers от 2 до 500, описание от 10 до 1000 символов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Игра создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Game> createGame(@Valid @RequestBody Game game) {
        Game saved = gameService.save(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Обновить игру", description = "Полностью обновляет игру по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Игра обновлена"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Игра не найдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@Parameter(description = "ID игры", example = "1") @PathVariable Long id, @Valid @RequestBody Game game) {
        gameService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Игра с id " + id + " не найдена"));
        game.setId(id);
        return ResponseEntity.ok(gameService.save(game));
    }

    @Operation(summary = "Удалить игру", description = "Удаляет игру по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Игра удалена"),
            @ApiResponse(responseCode = "404", description = "Игра не найдена")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@Parameter(description = "ID игры", example = "1") @PathVariable Long id) {
        gameService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Игра с id " + id + " не найдена"));
        gameService.deleteById(id);
    }
}