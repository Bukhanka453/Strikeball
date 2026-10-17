package com.example.strikeball.controller;

import com.example.strikeball.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class HomeController {

    private final GameService gameService;

    public HomeController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("upcomingGames", gameService.findAll());
        model.addAttribute("currentYear", LocalDate.now().getYear());
        return "index";
    }
}