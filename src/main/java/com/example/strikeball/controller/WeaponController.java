package com.example.strikeball.controller;

import com.example.strikeball.model.Weapon;
import com.example.strikeball.service.WeaponService;
import com.example.strikeball.service.WeaponTypeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/weapons")
public class WeaponController {

    private final WeaponService weaponService;
    private final WeaponTypeService weaponTypeService;

    public WeaponController(WeaponService weaponService,
                            WeaponTypeService weaponTypeService) {
        this.weaponService = weaponService;
        this.weaponTypeService = weaponTypeService;
    }

    @GetMapping
    public String list(@RequestParam(value = "search", required = false) String search, Model model) {
        model.addAttribute("weapons", weaponService.searchByName(search));
        model.addAttribute("search", search);
        return "weapons/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        return weaponService.findById(id)
                .map(weapon -> {
                    model.addAttribute("weapon", weapon);
                    return "weapons/detail";
                })
                .orElse("redirect:/weapons");
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("weapon", new Weapon());
        model.addAttribute("types", weaponTypeService.findAll());
        return "weapons/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("weapon") Weapon weapon,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", weaponTypeService.findAll());
            return "weapons/form";
        }
        weaponService.save(weapon);
        return "redirect:/weapons";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return weaponService.findById(id)
                .map(weapon -> {
                    model.addAttribute("weapon", weapon);
                    model.addAttribute("types", weaponTypeService.findAll());
                    return "weapons/form";
                })
                .orElse("redirect:/weapons");
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("weapon") Weapon weapon,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", weaponTypeService.findAll());
            return "weapons/form";
        }
        weapon.setId(id);
        weaponService.save(weapon);
        return "redirect:/weapons";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        weaponService.deleteById(id);
        return "redirect:/weapons";
    }
}