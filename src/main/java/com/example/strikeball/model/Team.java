package com.example.strikeball.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название команды обязательно")
    @Size(min = 2, max = 50, message = "Название от 2 до 50 символов")
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Size(max = 500, message = "Девиз до 500 символов")
    @Column(length = 500)
    private String motto;

    @NotNull(message = "Количество участников обязательно")
    @Min(value = 2, message = "В команде минимум 2 человека")
    @Max(value = 50, message = "В команде максимум 50 человек")
    private Integer membersCount;

    // СВЯЗЬ: капитан команды (много команд могут иметь одного капитана в теории — но обычно один)
    @NotNull(message = "Капитан команды обязателен")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "captain_id", nullable = false)
    private User captain;

    // геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMotto() { return motto; }
    public void setMotto(String motto) { this.motto = motto; }
    public Integer getMembersCount() { return membersCount; }
    public void setMembersCount(Integer membersCount) { this.membersCount = membersCount; }
    public User getCaptain() { return captain; }
    public void setCaptain(User captain) { this.captain = captain; }
}