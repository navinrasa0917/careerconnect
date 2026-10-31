package com.careerconnect.controller;

import com.careerconnect.entity.Application;
import com.careerconnect.entity.Job;
import com.careerconnect.entity.User;
import com.careerconnect.repository.ApplicationRepository;
import com.careerconnect.repository.JobRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    public HomeController(
            ApplicationRepository applicationRepository,
            JobRepository jobRepository) {

        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

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

        // Real application / placement drive data for the dashboard
        List<Application> inProgress =
                applicationRepository
                        .findByUserAndDriveDateIsNullOrderByAppliedAtDesc(user);

        List<Application> drives =
                applicationRepository
                        .findByUserAndDriveDateIsNotNullOrderByDriveDateAsc(user);

        LocalDate today = LocalDate.now();

        List<Application> upcoming = new ArrayList<>();

        for (Application a : drives) {
            if (!a.getDriveDate().isBefore(today)) {
                upcoming.add(a);
            }
        }

        model.addAttribute("applicationCount", inProgress.size() + drives.size());
        model.addAttribute("driveCount", upcoming.size());
        model.addAttribute("recentApplications",
                inProgress.subList(0, Math.min(3, inProgress.size())));
        model.addAttribute("upcomingDrives",
                upcoming.subList(0, Math.min(3, upcoming.size())));

        // Recommended jobs: real jobs from the database
        Set<Long> appliedJobIds = new HashSet<>();

        for (Application a : inProgress) {
            appliedJobIds.add(a.getJob().getId());
        }
        for (Application a : drives) {
            appliedJobIds.add(a.getJob().getId());
        }

        model.addAttribute("recommendedJobs",
                recommendJobs(user, appliedJobIds));

        return "home";
    }


    // Picks up to 3 open jobs the student has not applied for yet,
    // best match (skills, department) first, newest first on ties.
    private List<Job> recommendJobs(User user, Set<Long> appliedJobIds) {

        Set<String> studentSkills = splitWords(user.getSkills());

        List<Job> candidates = new ArrayList<>();

        for (Job job : jobRepository.findAll()) {

            if (appliedJobIds.contains(job.getId())) {
                continue;
            }

            if (job.getStatus() != null
                    && !job.getStatus().equalsIgnoreCase("Open")) {
                continue;
            }

            if (job.getApplicationDeadline() != null
                    && job.getApplicationDeadline().isBefore(LocalDate.now())) {
                continue;
            }

            // Skip jobs whose minimum CGPA the student does not meet
            if (job.getMinimumCgpa() != null && user.getCgpa() != null
                    && user.getCgpa() < job.getMinimumCgpa()) {
                continue;
            }

            candidates.add(job);
        }

        candidates.sort(
                Comparator
                        .comparingInt((Job j) -> -score(j, user, studentSkills))
                        .thenComparing(
                                Job::getPostedDate,
                                Comparator.nullsLast(Comparator.reverseOrder())));

        return candidates.subList(0, Math.min(3, candidates.size()));
    }

    private int score(Job job, User user, Set<String> studentSkills) {

        int score = 0;

        for (String skill : splitWords(job.getSkills())) {
            if (studentSkills.contains(skill)) {
                score += 2;
            }
        }

        if (user.getDepartment() != null && job.getEligibleDepartments() != null
                && job.getEligibleDepartments().toLowerCase()
                        .contains(user.getDepartment().trim().toLowerCase())) {
            score += 1;
        }

        return score;
    }

    private Set<String> splitWords(String text) {

        Set<String> words = new HashSet<>();

        if (text != null) {
            for (String part : text.split(",")) {
                if (!part.isBlank()) {
                    words.add(part.trim().toLowerCase());
                }
            }
        }

        return words;
    }
}