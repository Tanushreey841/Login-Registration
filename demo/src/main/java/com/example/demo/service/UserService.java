package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User save(User user) {

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {

            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean validatePassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    public void changePassword(String username, String currentPassword, String newPassword) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        if (!newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).{8,}$")) {
            throw new RuntimeException("Password must be 8+ chars with letter, number & special character");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    public User updateProfile(User updatedUser) {

        User existing = userRepository.findByUsername(updatedUser.getUsername());

        if (existing == null) {
            throw new RuntimeException("User not found");
        }

        existing.setEmail(updatedUser.getEmail());
        existing.setPhone(updatedUser.getPhone());
        existing.setGender(updatedUser.getGender());
        existing.setHobbies(updatedUser.getHobbies());
        existing.setDob(updatedUser.getDob());

        return userRepository.save(existing);
    }

    
}