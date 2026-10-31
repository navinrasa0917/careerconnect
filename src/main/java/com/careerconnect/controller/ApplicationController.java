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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        // Student already applied for this job -> just show applications
        if (applicationRepository.existsByUserAndJob(user, job)) {
            return "redirect:/applications";
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

        // Applications still in progress (not yet moved to a placement drive)
        List<Application> applications =
                applicationRepository
                        .findByUserAndDriveDateIsNullOrderByAppliedAtDesc(user);

        model.addAttribute(
                "applications",
                applications
        );

        // Number of applications already moved to Placement Drives
        model.addAttribute(
                "driveCount",
                applicationRepository
                        .findByUserAndDriveDateIsNotNullOrderByDriveDateAsc(user)
                        .size()
        );

        return "applications";
    }


    // ================= PLACEMENT DRIVES =================

    @GetMapping("/placement-drives")
    public String placementDrives(
            Model model,
            HttpSession session) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        LocalDate today = LocalDate.now();

        List<Application> upcoming = new ArrayList<>();
        List<Application> past = new ArrayList<>();

        for (Application a : applicationRepository
                .findByUserAndDriveDateIsNotNullOrderByDriveDateAsc(user)) {

            if (a.getDriveDate().isBefore(today)) {
                past.add(a);
            } else {
                upcoming.add(a);
            }
        }

        model.addAttribute("upcomingDrives", upcoming);
        model.addAttribute("pastDrives", past);

        return "placement-drives";
    }
}