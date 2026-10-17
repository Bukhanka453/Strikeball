package com.example.strikeball.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название игры обязательно")
    @Size(min = 3, max = 150, message = "Название от 3 до 150 символов")
    @Column(nullable = false, length = 150)
    private String name;

    @NotNull(message = "Дата игры обязательна")
    @FutureOrPresent(message = "Дата игры не может быть в прошлом")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "Максимум игроков обязателен")
    @Min(value = 2, message = "Минимум 2 игрока")
    @Max(value = 500, message = "Максимум 500 игроков")
    private Integer maxPlayers;

    @NotNull(message = "Текущее количество игроков обязательно")
    @Min(value = 0, message = "Не может быть отрицательным")
    private Integer currentPlayers = 0;

    @NotBlank(message = "Описание обязательно")
    @Size(min = 10, max = 1000, message = "Описание от 10 до 1000 символов")
    @Column(nullable = false, length = 1000)
    private String description;

    // СВЯЗЬ: полигон, на котором проходит игра
    @NotNull(message = "Полигон обязателен")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    // СВЯЗЬ: команда-организатор
    @NotNull(message = "Команда-организатор обязательна")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    // геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Integer getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(Integer maxPlayers) { this.maxPlayers = maxPlayers; }
    public Integer getCurrentPlayers() { return currentPlayers; }
    public void setCurrentPlayers(Integer currentPlayers) { this.currentPlayers = currentPlayers; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
}