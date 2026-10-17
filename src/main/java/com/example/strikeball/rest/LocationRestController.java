package com.example.strikeball.rest;

import com.example.strikeball.exception.ResourceNotFoundException;
import com.example.strikeball.model.Location;
import com.example.strikeball.service.LocationService;
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
@RequestMapping("/api/locations")
@Tag(name = "Полигоны", description = "Операции с полигонами (CRUD)")
@SecurityRequirement(name = "bearerAuth")
public class LocationRestController {

    private final LocationService locationService;

    public LocationRestController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(summary = "Получить список полигонов", description = "Возвращает список полигонов с фильтром по названию.")
    @ApiResponse(responseCode = "200", description = "Список полигонов получен")
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations(@Parameter(description = "Поиск по названию", example = "лес") @RequestParam(required = false) String search) {
        return ResponseEntity.ok(locationService.searchByName(search));
    }

    @Operation(summary = "Получить полигон по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Полигон найден"),
            @ApiResponse(responseCode = "404", description = "Полигон не найден", content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@Parameter(description = "ID полигона", example = "1") @PathVariable Long id) {
        return locationService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Полигон с id " + id + " не найден"));
    }

    @Operation(summary = "Создать полигон", description = "Поле owner должно содержать существующий ID пользователя. "
            + "Валидация: название от 3 до 100 символов, адрес до 255, " + "площадь от 1 до 10000 га.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Полигон создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Location> createLocation(@Valid @RequestBody Location location) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.save(location));
    }

    @Operation(summary = "Обновить полигон")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Полигон обновлён"),
            @ApiResponse(responseCode = "404", description = "Полигон не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@Parameter(description = "ID полигона", example = "1") @PathVariable Long id, @Valid @RequestBody Location location) {
        locationService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Полигон с id " + id + " не найден"));
        location.setId(id);
        return ResponseEntity.ok(locationService.save(location));
    }

    @Operation(summary = "Удалить полигон")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Полигон удалён"),
            @ApiResponse(responseCode = "404", description = "Полигон не найден")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocation(@Parameter(description = "ID полигона", example = "1") @PathVariable Long id) {
        locationService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Полигон с id " + id + " не найден"));
        locationService.deleteById(id);
    }
}