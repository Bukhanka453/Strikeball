package com.example.strikeball.rest;

import com.example.strikeball.exception.ResourceNotFoundException;
import com.example.strikeball.model.Team;
import com.example.strikeball.service.TeamService;
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
@RequestMapping("/api/teams")
@Tag(name = "Команды", description = "Операции с командами (CRUD)")
@SecurityRequirement(name = "bearerAuth")
public class TeamRestController {

    private final TeamService teamService;

    public TeamRestController(TeamService teamService) {
        this.teamService = teamService;
    }

    @Operation(summary = "Получить список команд", description = "Возвращает список команд с фильтром по названию.")
    @ApiResponse(responseCode = "200", description = "Список команд получен")
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams(@Parameter(description = "Поиск по названию", example = "волк") @RequestParam(required = false) String search) {
        return ResponseEntity.ok(teamService.searchByName(search));
    }

    @Operation(summary = "Получить команду по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команда найдена"),
            @ApiResponse(responseCode = "404", description = "Команда не найдена",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@Parameter(description = "ID команды", example = "1") @PathVariable Long id) {
        return teamService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Команда с id " + id + " не найдена"));
    }

    @Operation(summary = "Создать команду", description = "Поле captain должно содержать существующий ID пользователя. "
            + "Валидация: название от 2 до 50 символов, участников от 2 до 50.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Команда создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Team> createTeam(@Valid @RequestBody Team team) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.save(team));
    }

    @Operation(summary = "Обновить команду")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команда обновлена"),
            @ApiResponse(responseCode = "404", description = "Команда не найдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@Parameter(description = "ID команды", example = "1") @PathVariable Long id, @Valid @RequestBody Team team) {
        teamService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Команда с id " + id + " не найдена"));
        team.setId(id);
        return ResponseEntity.ok(teamService.save(team));
    }

    @Operation(summary = "Удалить команду")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Команда удалена"),
            @ApiResponse(responseCode = "404", description = "Команда не найдена")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@Parameter(description = "ID команды", example = "1") @PathVariable Long id) {
        teamService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Команда с id " + id + " не найдена"));
        teamService.deleteById(id);
    }
}