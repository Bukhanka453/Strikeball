package com.example.strikeball.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "weapon_types")
@Schema(description = "Тип оружия (автомат, пистолет и т.д.)")
public class WeaponType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID типа", example = "1")
    private Long id;

    @NotBlank(message = "Название типа обязательно")
    @Size(min = 2, max = 50, message = "Название от 2 до 50 символов")
    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "Название типа", example = "Автомат")
    private String name;

    @Size(max = 300, message = "Описание до 300 символов")
    @Column(length = 300)
    @Schema(description = "Описание типа", example = "Автоматические приводы")
    private String description;

    public WeaponType() {}

    public WeaponType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}