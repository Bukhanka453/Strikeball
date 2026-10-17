package com.example.strikeball.rest;

import com.example.strikeball.exception.ResourceNotFoundException;
import com.example.strikeball.model.Weapon;
import com.example.strikeball.service.WeaponService;
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
@RequestMapping("/api/weapons")
@Tag(name = "Оружие", description = "Операции со страйкбольным оружием (CRUD)")
@SecurityRequirement(name = "bearerAuth")
public class WeaponRestController {

    private final WeaponService weaponService;

    public WeaponRestController(WeaponService weaponService) {
        this.weaponService = weaponService;
    }

    @Operation(summary = "Получить список оружия",
            description = "Возвращает список с фильтром по названию.")
    @ApiResponse(responseCode = "200", description = "Список получен")
    @GetMapping
    public ResponseEntity<List<Weapon>> getAll(
            @Parameter(description = "Поиск по названию", example = "AK")
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(weaponService.searchByName(search));
    }

    @Operation(summary = "Получить оружие по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Оружие найдено"),
            @ApiResponse(responseCode = "404", description = "Оружие не найдено",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Weapon> getById(
            @Parameter(description = "ID оружия", example = "1") @PathVariable Long id) {
        return weaponService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Оружие с id " + id + " не найдено"));
    }

    @Operation(summary = "Создать оружие",
            description = "Поле type должно содержать существующий ID типа оружия.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Оружие создано"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Weapon> create(@Valid @RequestBody Weapon weapon) {
        return ResponseEntity.status(HttpStatus.CREATED).body(weaponService.save(weapon));
    }

    @Operation(summary = "Обновить оружие")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Оружие обновлено"),
            @ApiResponse(responseCode = "404", description = "Оружие не найдено")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Weapon> update(
            @Parameter(description = "ID оружия", example = "1") @PathVariable Long id,
            @Valid @RequestBody Weapon weapon) {
        weaponService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Оружие с id " + id + " не найдено"));
        weapon.setId(id);
        return ResponseEntity.ok(weaponService.save(weapon));
    }

    @Operation(summary = "Удалить оружие")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Оружие удалено"),
            @ApiResponse(responseCode = "404", description = "Оружие не найдено")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "ID оружия", example = "1") @PathVariable Long id) {
        weaponService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Оружие с id " + id + " не найдено"));
        weaponService.deleteById(id);
    }
}