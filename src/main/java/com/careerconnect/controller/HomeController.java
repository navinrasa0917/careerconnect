package com.careerconnect.controller;

import com.careerconnect.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {

        // Get the currently logged-in student
        User user = (User) session.getAttribute("loggedInUser");

        // If no student is logged in, go to login page
        if (user == null) {
            return "redirect:/login";
        }

        // Send logged-in student's name to home page
        model.addAttribute("name", user.getName());

        return "home";
    }
}