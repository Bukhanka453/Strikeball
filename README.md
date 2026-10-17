# Strikeball Arena

Веб-приложение для организации страйкбольных игр с полноценной админ-панелью, REST API и JWT-авторизацией.

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-blue)
![Gradle](https://img.shields.io/badge/Gradle-8.14-darkgreen)

---

## О проекте

**Strikeball Arena** — это платформа, где игроки могут находить команды, организаторы — создавать игры на разных полигонах, а администраторы — управлять всей базой данных через единую панель.

Проект реализует полный цикл работы с данными: от регистрации пользователя и разграничения прав до создания игр с привязкой к полигонам и командам.

### Возможности

- **Регистрация и авторизация** (Spring Security + BCrypt)
- **Три роли доступа**: `ADMIN`, `ORGANIZER`, `PLAYER`
- **Управление играми**, командами, полигонами
- **Справочник оружия** и типов оружия
- **Админ-панель** для управления всеми 8 таблицами БД
- **Поиск** по названию для каждой сущности
- **Валидация** всех полей через Jakarta Bean Validation
- **REST API** с документацией в Swagger UI
- **JWT-авторизация** для API

---

## Технологический стек

| Категория | Технология |
|---|---|
| Язык | Java 17+ |
| Фреймворк | Spring Boot 3.2.5 |
| Сборка | Gradle 8.14 |
| БД | PostgreSQL 14+ |
| ORM | Hibernate 6.4 + Spring Data JPA |
| Безопасность | Spring Security 6.2 + BCrypt + JJWT 0.12.3 |
| Шаблоны | Thymeleaf 3.1 |
| Документация | SpringDoc OpenAPI 2.5 + Swagger UI 5.13 |
| Стили | Чистый CSS (без фреймворков) |

---

## Структура проекта

```text
strikeball/
├── build.gradle
├── gradle/
├── gradlew
├── gradlew.bat
└── src/
    ├── main/
    │   ├── java/com/example/strikeball/
    │   │   ├── StrikeballApplication.java   # Точка входа
    │   │   ├── config/
    │   │   │   ├── SecurityConfig.java      # Spring Security
    │   │   │   └── OpenApiConfig.java       # Swagger
    │   │   ├── controller/                  # HTML-контроллеры
    │   │   ├── rest/                        # REST API
    │   │   ├── security/                    # JWT
    │   │   ├── exception/                   # Обработка ошибок
    │   │   ├── model/                       # JPA-сущности
    │   │   ├── repository/                  # Spring Data JPA
    │   │   └── service/                     # Бизнес-логика
    │   └── resources/
    │       ├── application.properties
    │       ├── static/css/style.css
    │       └── templates/                   # Thymeleaf-шаблоны
    └── test/
```
---

## База данных

Всего **8 таблиц**:

| Таблица | Назначение |
|---|---|
| `users` | Пользователи |
| `roles` | Роли (ADMIN, ORGANIZER, PLAYER) |
| `users_roles` | Связь ManyToMany |
| `games` | Игры |
| `teams` | Команды |
| `locations` | Полигоны |
| `weapons` | Оружие |
| `weapon_types` | Типы оружия |

### Схема связей
```text
users ──< users_roles >── roles
│
├──< teams (captain_id)
├──< locations (owner_id)
└──< games
├── location_id → locations
└── team_id → teams

weapon_types ──< weapons (type_id)
```
