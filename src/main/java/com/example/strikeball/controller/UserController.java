package com.example.strikeball.controller;

import com.example.strikeball.model.User;
import com.example.strikeball.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(@RequestParam(value = "search", required = false) String search, Model model) {
        model.addAttribute("users", userService.searchByNickname(search));
        model.addAttribute("search", search);
        return "users/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "users/detail";
                })
                .orElse("redirect:/users");
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("user") User user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/form";
        }
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "users/form";
                })
                .orElse("redirect:/users");
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/form";
        }
        user.setId(id);
        userService.save(user);
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}