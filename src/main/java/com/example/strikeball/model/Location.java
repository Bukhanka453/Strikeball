package com.example.strikeball.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название полигона обязательно")
    @Size(min = 3, max = 100, message = "Название от 3 до 100 символов")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Адрес обязателен")
    @Size(max = 255, message = "Адрес слишком длинный")
    @Column(nullable = false)
    private String address;

    @NotNull(message = "Площадь обязательна")
    @Min(value = 1, message = "Площадь должна быть больше 0")
    @Max(value = 10000, message = "Слишком большая площадь")
    private Integer areaHectares;

    @Size(max = 500, message = "Описание до 500 символов")
    @Column(length = 500)
    private String description;

    // СВЯЗЬ: ответственный за полигон
    @NotNull(message = "Ответственный за полигон обязателен")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getAreaHectares() { return areaHectares; }
    public void setAreaHectares(Integer areaHectares) { this.areaHectares = areaHectares; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}