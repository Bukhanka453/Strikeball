package com.example.strikeball.controller;

import com.example.strikeball.model.Game;
import com.example.strikeball.service.GameService;
import com.example.strikeball.service.LocationService;
import com.example.strikeball.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final LocationService locationService;
    private final TeamService teamService;

    public GameController(GameService gameService,
                          LocationService locationService,
                          TeamService teamService) {
        this.gameService = gameService;
        this.locationService = locationService;
        this.teamService = teamService;
    }

    @GetMapping
    public String list(@RequestParam(value = "search", required = false) String search, Model model) {
        model.addAttribute("games", gameService.searchByName(search));
        model.addAttribute("search", search);
        return "games/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        return gameService.findById(id)
                .map(game -> {
                    model.addAttribute("game", game);
                    return "games/detail";
                })
                .orElse("redirect:/games");
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("game", new Game());
        // Передаём списки для выпадающих списков
        model.addAttribute("locations", locationService.findAll());
        model.addAttribute("teams", teamService.findAll());
        return "games/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("game") Game game, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // При ошибке нужно снова передать списки, иначе селекты будут пустыми
            model.addAttribute("locations", locationService.findAll());
            model.addAttribute("teams", teamService.findAll());
            return "games/form";
        }
        gameService.save(game);
        return "redirect:/games";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return gameService.findById(id)
                .map(game -> {
                    model.addAttribute("game", game);
                    model.addAttribute("locations", locationService.findAll());
                    model.addAttribute("teams", teamService.findAll());
                    return "games/form";
                })
                .orElse("redirect:/games");
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("game") Game game, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("locations", locationService.findAll());
            model.addAttribute("teams", teamService.findAll());
            return "games/form";
        }
        game.setId(id);
        gameService.save(game);
        return "redirect:/games";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        gameService.deleteById(id);
        return "redirect:/games";
    }
}