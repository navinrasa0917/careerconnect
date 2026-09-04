package com.careerconnect.controller;

import com.careerconnect.entity.User;
import com.careerconnect.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ================= HOME PAGE =================

    @GetMapping("/")
    public String homePage() {
        return "home";
    }


    // ================= LOGIN PAGE =================

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    // ================= SIGNUP PAGE =================

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }


    // ================= SIGNUP =================

    @PostMapping("/signup")
    public String signup(
            @ModelAttribute User user,
            Model model) {

        userService.signup(user);

        model.addAttribute(
                "message",
                "Account created successfully!"
        );

        return "login";
    }


    // ================= LOGIN =================

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        User user = userService.login(email, password);

        if (user != null) {

            // Store the logged-in student in session
            session.setAttribute("loggedInUser", user);

            System.out.println(
                    "LOGIN SUCCESS: " + user.getEmail()
            );

            return "redirect:/home";
        }

        model.addAttribute(
                "error",
                "Invalid email or password!"
        );

        return "login";
    }


    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}