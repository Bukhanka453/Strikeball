package com.example.strikeball.controller;

import com.example.strikeball.model.Team;
import com.example.strikeball.service.TeamService;
import com.example.strikeball.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;

    public TeamController(TeamService teamService, UserService userService) {
        this.teamService = teamService;
        this.userService = userService;
    }

    @GetMapping
    public String list(@RequestParam(value = "search", required = false) String search, Model model) {
        model.addAttribute("teams", teamService.searchByName(search));
        model.addAttribute("search", search);
        return "teams/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        return teamService.findById(id)
                .map(team -> {
                    model.addAttribute("team", team);
                    return "teams/detail";
                })
                .orElse("redirect:/teams");
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("team", new Team());
        model.addAttribute("users", userService.findAll());
        return "teams/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("team") Team team,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "teams/form";
        }
        teamService.save(team);
        return "redirect:/teams";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return teamService.findById(id)
                .map(team -> {
                    model.addAttribute("team", team);
                    model.addAttribute("users", userService.findAll());
                    return "teams/form";
                })
                .orElse("redirect:/teams");
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("team") Team team, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "teams/form";
        }
        team.setId(id);
        teamService.save(team);
        return "redirect:/teams";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        teamService.deleteById(id);
        return "redirect:/teams";
    }
}