package com.careerconnect.controller;

import com.careerconnect.entity.User;
import com.careerconnect.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // View Profile
    @GetMapping("/profile")
    public String profile(
            Model model,
            HttpSession session) { 

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "profile";
    }


    // Show Edit Profile Page
    @GetMapping("/profile/edit")
    public String editProfile(
            Model model,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "edit-profile";
    }


    // Save Updated Profile
   @PostMapping("/profile/update")
 public String updateProfile(
            @ModelAttribute User user,
            HttpSession session) {

        // Get the currently logged-in student
        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        loggedInUser.setName(user.getName());
        loggedInUser.setPhone(user.getPhone());
        loggedInUser.setDepartment(user.getDepartment());
        loggedInUser.setCollege(user.getCollege());
        loggedInUser.setYear(user.getYear());
        loggedInUser.setCgpa(user.getCgpa());
        loggedInUser.setSkills(user.getSkills());
        loggedInUser.setCareerGoal(user.getCareerGoal());
        loggedInUser.setAboutMe(user.getAboutMe());
        loggedInUser.setHobbies(user.getHobbies());
        loggedInUser.setAchievements(user.getAchievements());
        loggedInUser.setFutureGoals(user.getFutureGoals());

        userRepository.save(loggedInUser);


          session.setAttribute("loggedInUser", loggedInUser);

        System.out.println("PROFILE SAVED!");

        return "redirect:/profile";
    }
}

       