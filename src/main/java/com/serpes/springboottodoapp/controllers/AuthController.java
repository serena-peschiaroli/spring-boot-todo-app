package com.serpes.springboottodoapp.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.serpes.springboottodoapp.dto.RegistrationForm;
import com.serpes.springboottodoapp.models.AppUser;
import com.serpes.springboottodoapp.repositories.AppUserRepository;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registrationPage(@ModelAttribute("registrationForm") RegistrationForm registrationForm) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationForm") RegistrationForm registrationForm,
            BindingResult result) {
        if (!registrationForm.getPassword().equals(registrationForm.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
        }

        if (appUserRepository.existsByUsername(registrationForm.getUsername())) {
            result.rejectValue("username", "username.exists", "Username is already taken");
        }

        if (result.hasErrors()) {
            return "register";
        }

        AppUser user = new AppUser(
                registrationForm.getUsername(),
                passwordEncoder.encode(registrationForm.getPassword()),
                "USER");
        appUserRepository.save(user);

        return "redirect:/login?registered";
    }
}
