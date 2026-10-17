package com.example.strikeball.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "weapons")
@Schema(description = "Страйкбольное оружие (привод)")
public class Weapon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID оружия", example = "1")
    private Long id;

    @NotBlank(message = "Название обязательно")
    @Size(min = 2, max = 100, message = "Название от 2 до 100 символов")
    @Column(nullable = false, length = 100)
    @Schema(description = "Название модели", example = "AK-74M")
    private String name;

    @NotNull(message = "Тип оружия обязателен")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false)
    @Schema(description = "Тип оружия")
    private WeaponType type;

    @Size(max = 20, message = "Калибр до 20 символов")
    @Column(length = 20)
    @Schema(description = "Калибр", example = "6 мм")
    private String caliber;

    @Min(value = 0, message = "Цена не может быть отрицательной")
    @Schema(description = "Цена в рублях", example = "25000")
    private Integer price;

    @Size(max = 500, message = "Описание до 500 символов")
    @Column(length = 500)
    @Schema(description = "Описание оружия")
    private String description;

    public Weapon() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public WeaponType getType() { return type; }
    public void setType(WeaponType type) { this.type = type; }
    public String getCaliber() { return caliber; }
    public void setCaliber(String caliber) { this.caliber = caliber; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}