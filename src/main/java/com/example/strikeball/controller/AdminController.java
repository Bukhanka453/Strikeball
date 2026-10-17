package com.example.strikeball.controller;

import com.example.strikeball.model.*;
import com.example.strikeball.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final GameService gameService;
    private final TeamService teamService;
    private final LocationService locationService;
    private final WeaponService weaponService;
    private final WeaponTypeService weaponTypeService;

    public AdminController(UserService userService,
                           RoleService roleService,
                           GameService gameService,
                           TeamService teamService,
                           LocationService locationService,
                           WeaponService weaponService,
                           WeaponTypeService weaponTypeService) {
        this.userService = userService;
        this.roleService = roleService;
        this.gameService = gameService;
        this.teamService = teamService;
        this.locationService = locationService;
        this.weaponService = weaponService;
        this.weaponTypeService = weaponTypeService;
    }

    // ==================== ГЛАВНАЯ ====================
    @GetMapping
    public String dashboard() {
        return "admin/dashboard";
    }

    // ==================== USERS ====================
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/users/{id}/roles")
    public String editUserRoles(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/user-roles";
    }

    @PostMapping("/users/{id}/roles")
    public String updateUserRoles(@PathVariable Long id,
                                  @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        Set<Role> newRoles = new HashSet<>();
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                roleService.findById(roleId).ifPresent(newRoles::add);
            }
        }
        user.setRoles(newRoles);
        userService.save(user);
        return "redirect:/admin/users";
    }

    // ==================== ROLES ====================
    @GetMapping("/roles")
    public String listRoles(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "admin/roles/list";
    }

    @GetMapping("/roles/new")
    public String showRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "admin/roles/form";
    }

    @PostMapping("/roles")
    public String createRole(@Valid @ModelAttribute("role") Role role,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "admin/roles/form";
        roleService.save(role);
        return "redirect:/admin/roles";
    }

    @GetMapping("/roles/{id}/edit")
    public String editRole(@PathVariable Long id, Model model) {
        return roleService.findById(id)
                .map(role -> {
                    model.addAttribute("role", role);
                    return "admin/roles/form";
                })
                .orElse("redirect:/admin/roles");
    }

    @PostMapping("/roles/{id}")
    public String updateRole(@PathVariable Long id,
                             @Valid @ModelAttribute("role") Role role,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "admin/roles/form";
        role.setId(id);
        roleService.save(role);
        return "redirect:/admin/roles";
    }

    @PostMapping("/roles/{id}/delete")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteById(id);
        return "redirect:/admin/roles";
    }

    // ==================== GAMES ====================
    @GetMapping("/games")
    public String listGames(Model model) {
        model.addAttribute("games", gameService.findAll());
        return "admin/games/list";
    }

    @GetMapping("/games/new")
    public String showGameForm(Model model) {
        model.addAttribute("game", new Game());
        model.addAttribute("locations", locationService.findAll());
        model.addAttribute("teams", teamService.findAll());
        return "admin/games/form";
    }

    @PostMapping("/games")
    public String createGame(@Valid @ModelAttribute("game") Game game,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("locations", locationService.findAll());
            model.addAttribute("teams", teamService.findAll());
            return "admin/games/form";
        }
        gameService.save(game);
        return "redirect:/admin/games";
    }

    @GetMapping("/games/{id}/edit")
    public String editGame(@PathVariable Long id, Model model) {
        return gameService.findById(id)
                .map(game -> {
                    model.addAttribute("game", game);
                    model.addAttribute("locations", locationService.findAll());
                    model.addAttribute("teams", teamService.findAll());
                    return "admin/games/form";
                })
                .orElse("redirect:/admin/games");
    }

    @PostMapping("/games/{id}")
    public String updateGame(@PathVariable Long id,
                             @Valid @ModelAttribute("game") Game game,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("locations", locationService.findAll());
            model.addAttribute("teams", teamService.findAll());
            return "admin/games/form";
        }
        game.setId(id);
        gameService.save(game);
        return "redirect:/admin/games";
    }

    @PostMapping("/games/{id}/delete")
    public String deleteGame(@PathVariable Long id) {
        gameService.deleteById(id);
        return "redirect:/admin/games";
    }

    // ==================== TEAMS ====================
    @GetMapping("/teams")
    public String listTeams(Model model) {
        model.addAttribute("teams", teamService.findAll());
        return "admin/teams/list";
    }

    @GetMapping("/teams/new")
    public String showTeamForm(Model model) {
        model.addAttribute("team", new Team());
        model.addAttribute("users", userService.findAll());
        return "admin/teams/form";
    }

    @PostMapping("/teams")
    public String createTeam(@Valid @ModelAttribute("team") Team team,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "admin/teams/form";
        }
        teamService.save(team);
        return "redirect:/admin/teams";
    }

    @GetMapping("/teams/{id}/edit")
    public String editTeam(@PathVariable Long id, Model model) {
        return teamService.findById(id)
                .map(team -> {
                    model.addAttribute("team", team);
                    model.addAttribute("users", userService.findAll());
                    return "admin/teams/form";
                })
                .orElse("redirect:/admin/teams");
    }

    @PostMapping("/teams/{id}")
    public String updateTeam(@PathVariable Long id,
                             @Valid @ModelAttribute("team") Team team,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "admin/teams/form";
        }
        team.setId(id);
        teamService.save(team);
        return "redirect:/admin/teams";
    }

    @PostMapping("/teams/{id}/delete")
    public String deleteTeam(@PathVariable Long id) {
        teamService.deleteById(id);
        return "redirect:/admin/teams";
    }

    // ==================== LOCATIONS ====================
    @GetMapping("/locations")
    public String listLocations(Model model) {
        model.addAttribute("locations", locationService.findAll());
        return "admin/locations/list";
    }

    @GetMapping("/locations/new")
    public String showLocationForm(Model model) {
        model.addAttribute("location", new Location());
        model.addAttribute("users", userService.findAll());
        return "admin/locations/form";
    }

    @PostMapping("/locations")
    public String createLocation(@Valid @ModelAttribute("location") Location location,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "admin/locations/form";
        }
        locationService.save(location);
        return "redirect:/admin/locations";
    }

    @GetMapping("/locations/{id}/edit")
    public String editLocation(@PathVariable Long id, Model model) {
        return locationService.findById(id)
                .map(location -> {
                    model.addAttribute("location", location);
                    model.addAttribute("users", userService.findAll());
                    return "admin/locations/form";
                })
                .orElse("redirect:/admin/locations");
    }

    @PostMapping("/locations/{id}")
    public String updateLocation(@PathVariable Long id,
                                 @Valid @ModelAttribute("location") Location location,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "admin/locations/form";
        }
        location.setId(id);
        locationService.save(location);
        return "redirect:/admin/locations";
    }

    @PostMapping("/locations/{id}/delete")
    public String deleteLocation(@PathVariable Long id) {
        locationService.deleteById(id);
        return "redirect:/admin/locations";
    }

    // ==================== WEAPONS ====================
    @GetMapping("/weapons")
    public String listWeapons(Model model) {
        model.addAttribute("weapons", weaponService.findAll());
        return "admin/weapons/list";
    }

    @GetMapping("/weapons/new")
    public String showWeaponForm(Model model) {
        model.addAttribute("weapon", new Weapon());
        model.addAttribute("types", weaponTypeService.findAll());
        return "admin/weapons/form";
    }

    @PostMapping("/weapons")
    public String createWeapon(@Valid @ModelAttribute("weapon") Weapon weapon,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", weaponTypeService.findAll());
            return "admin/weapons/form";
        }
        weaponService.save(weapon);
        return "redirect:/admin/weapons";
    }

    @GetMapping("/weapons/{id}/edit")
    public String editWeapon(@PathVariable Long id, Model model) {
        return weaponService.findById(id)
                .map(weapon -> {
                    model.addAttribute("weapon", weapon);
                    model.addAttribute("types", weaponTypeService.findAll());
                    return "admin/weapons/form";
                })
                .orElse("redirect:/admin/weapons");
    }

    @PostMapping("/weapons/{id}")
    public String updateWeapon(@PathVariable Long id,
                               @Valid @ModelAttribute("weapon") Weapon weapon,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", weaponTypeService.findAll());
            return "admin/weapons/form";
        }
        weapon.setId(id);
        weaponService.save(weapon);
        return "redirect:/admin/weapons";
    }

    @PostMapping("/weapons/{id}/delete")
    public String deleteWeapon(@PathVariable Long id) {
        weaponService.deleteById(id);
        return "redirect:/admin/weapons";
    }

    // ==================== WEAPON TYPES ====================
    @GetMapping("/weapon-types")
    public String listWeaponTypes(Model model) {
        model.addAttribute("weaponTypes", weaponTypeService.findAll());
        return "admin/weapon-types/list";
    }

    @GetMapping("/weapon-types/new")
    public String showWeaponTypeForm(Model model) {
        model.addAttribute("weaponType", new WeaponType());
        return "admin/weapon-types/form";
    }

    @PostMapping("/weapon-types")
    public String createWeaponType(@Valid @ModelAttribute("weaponType") WeaponType weaponType,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "admin/weapon-types/form";
        weaponTypeService.save(weaponType);
        return "redirect:/admin/weapon-types";
    }

    @GetMapping("/weapon-types/{id}/edit")
    public String editWeaponType(@PathVariable Long id, Model model) {
        return weaponTypeService.findById(id)
                .map(wt -> {
                    model.addAttribute("weaponType", wt);
                    return "admin/weapon-types/form";
                })
                .orElse("redirect:/admin/weapon-types");
    }

    @PostMapping("/weapon-types/{id}")
    public String updateWeaponType(@PathVariable Long id,
                                   @Valid @ModelAttribute("weaponType") WeaponType weaponType,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "admin/weapon-types/form";
        weaponType.setId(id);
        weaponTypeService.save(weaponType);
        return "redirect:/admin/weapon-types";
    }

    @PostMapping("/weapon-types/{id}/delete")
    public String deleteWeaponType(@PathVariable Long id) {
        weaponTypeService.deleteById(id);
        return "redirect:/admin/weapon-types";
    }
}