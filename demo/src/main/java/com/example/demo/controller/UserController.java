package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

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
    public String registerUser(@Valid @ModelAttribute User user,BindingResult result,Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username", "error.user", "Username already exists!");
            return "register";
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            result.rejectValue("email", "error.user", "Email already exists!");
            return "register";
        }

        user.setStatus("ACTIVE");

   
        userService.save(user);

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

        if (existing != null) {

         
            if (!"ACTIVE".equalsIgnoreCase(existing.getStatus())) {
                model.addAttribute("error", "Account is inactive!");
                return "login";
            }

          
            if (userService.validatePassword(user.getPassword(), existing.getPassword())) {
                return "redirect:/home?username=" + existing.getUsername();
            }
        }

        model.addAttribute("error", "Invalid username or password!");
        return "login";
    }

    @GetMapping("/home")
public String homePage(Model model, @RequestParam(required = false) String username) {

    if (username == null || username.isEmpty()) {
        return "redirect:/login";
    }

    User user = userService.findByUsername(username);

    model.addAttribute("user", user);
    model.addAttribute("username", username);

    return "home";
}
    
    @GetMapping("/profile")
public String showProfile(@RequestParam(required = false) String username, Model model) {

    if (username == null || username.isEmpty()) {
        return "redirect:/login";
    }

    User user = userService.findByUsername(username);

    model.addAttribute("user", user);
    model.addAttribute("username", username);

    return "profile";
}

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user, Model model) {

        User existing = userService.findByUsername(user.getUsername());

        if (existing == null) {
            model.addAttribute("error", "User not found!");
            return "profile";
        }

        existing.setEmail(user.getEmail());
        existing.setPhone(user.getPhone());
        existing.setGender(user.getGender());
        existing.setHobbies(user.getHobbies());
        existing.setDob(user.getDob());

        userService.save(existing);

        model.addAttribute("message", "Profile updated successfully!");
        model.addAttribute("user", existing);
        model.addAttribute("username", existing.getUsername()); 


        return "profile";
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(@RequestParam String username, Model model) {
        model.addAttribute("username", username);
        return "change-password";
    }
    @PostMapping("/change-password")
public String changePassword(@RequestParam String username,
                             @RequestParam String currentPassword,
                             @RequestParam String newPassword,
                             @RequestParam String confirmPassword,
                             Model model) {

    if (!newPassword.equals(confirmPassword)) {
        model.addAttribute("error", "Passwords do not match!");
        model.addAttribute("username", username);
        return "change-password";
    }

    try {
        userService.changePassword(username, currentPassword, newPassword);

        return "redirect:/home?username=" + username;

    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("username", username);
        return "change-password";
    }
}
    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("message", "Logged out successfully!");
        return "login";
    }

    
}