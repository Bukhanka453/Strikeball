package com.example.strikeball.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Никнейм обязателен")
    @Size(min = 3, max = 30, message = "Никнейм от 3 до 30 символов")
    @Column(nullable = false, unique = true, length = 30)
    private String nickname;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    @Size(max = 100, message = "Email слишком длинный")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, max = 100, message = "Пароль от 6 до 100 символов")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "Возраст обязателен")
    @Min(value = 12, message = "Минимальный возраст — 12 лет")
    @Max(value = 100, message = "Максимальный возраст — 100 лет")
    private Integer age;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Телефон должен содержать 10-15 цифр")
    @Column(length = 20)
    private String phone;

    // СВЯЗЬ: пользователь ↔ роли (многие-ко-многим)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}