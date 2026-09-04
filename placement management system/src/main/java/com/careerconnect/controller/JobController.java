package com.careerconnect.controller;

import com.careerconnect.entity.Job;
import com.careerconnect.repository.JobRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class JobController {

    private final JobRepository jobRepository;

    public JobController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    // View all jobs
    @GetMapping("/jobs")
    public String jobs(Model model) {

        List<Job> jobs = jobRepository.findAll();

        model.addAttribute("jobs", jobs);

        return "jobs";
    }

    // Show Post Job page
    @GetMapping("/jobs/post")
    public String postJobPage(Model model) {

        model.addAttribute("job", new Job());

        return "post-job";
    }

    // Save Job
    @PostMapping("/jobs/post")
    public String postJob(@ModelAttribute Job job) {

        jobRepository.save(job);

        return "redirect:/jobs";
    }

   @GetMapping("/jobs/{id}")
public String jobDetails(
        @PathVariable Long id,
        Model model) {

    Job job = jobRepository.findById(id)
            .orElse(null);

    if (job == null) {
        return "redirect:/jobs";
    }

    model.addAttribute("job", job);

    return "job-details";
}
}