package com.example.strikeball.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название роли обязательно")
    @Size(min = 3, max = 30, message = "Название роли от 3 до 30 символов")
    @Column(nullable = false, unique = true, length = 30)
    private String name;   // ROLE_ADMIN, ROLE_ORGANIZER, ROLE_PLAYER

    @Size(max = 200, message = "Описание до 200 символов")
    @Column(length = 200)
    private String description;

    public Role() {}

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return id != null && id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}