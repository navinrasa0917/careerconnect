package com.careerconnect.controller;

import com.careerconnect.entity.Application;
import com.careerconnect.entity.Job;
import com.careerconnect.entity.User;
import com.careerconnect.repository.ApplicationRepository;
import com.careerconnect.repository.JobRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ApplicationController {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    public ApplicationController(
            ApplicationRepository applicationRepository,
            JobRepository jobRepository) {

        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }


    // ================= APPLY PAGE =================

    @GetMapping("/jobs/{id}/apply")
    public String applyPage(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        // Check whether student is logged in
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Job job = jobRepository.findById(id)
                .orElse(null);

        if (job == null) {
            return "redirect:/jobs";
        }

        model.addAttribute("job", job);

        return "application";
    }


    // ================= SUBMIT APPLICATION =================

    @PostMapping("/jobs/{id}/apply")
    public String submitApplication(
            @PathVariable Long id,
            @ModelAttribute Application application,
            HttpSession session) {

        // Get the student who is actually logged in
        User user =
                (User) session.getAttribute("loggedInUser");

        // If nobody is logged in
        if (user == null) {
            return "redirect:/login";
        }

        Job job = jobRepository.findById(id)
                .orElse(null);

        if (job == null) {
            return "redirect:/jobs";
        }

        // Connect application with logged-in student
        application.setUser(user);

        // Connect application with selected job
        application.setJob(job);

        // Set application details
        application.setStatus("Applied");
        application.setAppliedAt(LocalDateTime.now());

        // Save application
        applicationRepository.save(application);

        System.out.println(
                "APPLICATION SAVED FOR: "
                        + user.getEmail()
        );

        return "redirect:/applications";
    }


    // ================= MY APPLICATIONS =================

    @GetMapping("/applications")
    public String myApplications(
            Model model,
            HttpSession session) {

        // Get currently logged-in student
        User user =
                (User) session.getAttribute("loggedInUser");

        // If nobody is logged in
        if (user == null) {
            return "redirect:/login";
        }

        // Get only this student's applications
        List<Application> applications =
                applicationRepository.findByUser(user);

        model.addAttribute(
                "applications",
                applications
        );

        return "applications";
    }
}