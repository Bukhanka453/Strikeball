package com.example.strikeball.controller;

import com.example.strikeball.model.Location;
import com.example.strikeball.service.LocationService;
import com.example.strikeball.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final UserService userService;

    public LocationController(LocationService locationService, UserService userService) {
        this.locationService = locationService;
        this.userService = userService;
    }

    @GetMapping
    public String list(@RequestParam(value = "search", required = false) String search, Model model) {
        model.addAttribute("locations", locationService.searchByName(search));
        model.addAttribute("search", search);
        return "locations/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        return locationService.findById(id)
                .map(location -> {
                    model.addAttribute("location", location);
                    return "locations/detail";
                })
                .orElse("redirect:/locations");
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("location", new Location());
        model.addAttribute("users", userService.findAll());
        return "locations/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("location") Location location,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "locations/form";
        }
        locationService.save(location);
        return "redirect:/locations";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return locationService.findById(id)
                .map(location -> {
                    model.addAttribute("location", location);
                    model.addAttribute("users", userService.findAll());
                    return "locations/form";
                })
                .orElse("redirect:/locations");
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("location") Location location, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "locations/form";
        }
        location.setId(id);
        locationService.save(location);
        return "redirect:/locations";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        locationService.deleteById(id);
        return "redirect:/locations";
    }
}