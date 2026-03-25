package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "register";
        }
        
        if(userService.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username", "error.user", "Username already exists!");
            return "register";
        }
        if(userService.findByEmail(user.getEmail()) != null) {
            result.rejectValue("email", "error.user", "Email already exists!");
            return "register";
        }
        userService.save(user);
        model.addAttribute("user", new User());
        model.addAttribute("message", "Registration successful! Please login.");
        return "login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model) {
        
        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
            user.getPassword() == null || user.getPassword().isEmpty()) {
            model.addAttribute("error", "Username and password are required!");
            return "login";
        }
        
        User existing = userService.findByUsername(user.getUsername());
        if (existing != null && userService.validatePassword(user.getPassword(), existing.getPassword())) {
            model.addAttribute("username", user.getUsername());
            return "home";
        }
        model.addAttribute("error", "Invalid username or password!");
        return "login";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }
}