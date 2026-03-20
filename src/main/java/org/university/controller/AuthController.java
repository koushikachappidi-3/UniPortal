package org.university.controller;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.university.dto.RegisterRequest;
import org.university.model.AppUser;
import org.university.repo.AppUserRepository;

@Controller
public class AuthController {

    private final AppUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------------- LOGIN ----------------

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ---------------- REGISTER ----------------

    // Show register page
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    // Handle registration (with strong password validation)
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult result,
            Model model) {

        // Validation errors (including strong password)
        if (result.hasErrors()) {
            return "register";
        }

        // Username uniqueness check
        if (userRepo.existsByUsername(request.getUsername())) {
            result.rejectValue(
                    "username",
                    "error.username",
                    "Username already exists"
            );
            return "register";
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Create user
        AppUser user = new AppUser(
                request.getUsername(),
                hashedPassword,
                "STUDENT"
        );

        userRepo.save(user);

        return "redirect:/login";
    }
}
