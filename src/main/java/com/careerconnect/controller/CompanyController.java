package com.careerconnect.controller;

import com.careerconnect.entity.Application;
import com.careerconnect.entity.Company;
import com.careerconnect.entity.Job;
import com.careerconnect.repository.ApplicationRepository;
import com.careerconnect.repository.CompanyRepository;
import com.careerconnect.repository.JobRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public CompanyController(
            CompanyRepository companyRepository,
            JobRepository jobRepository,
            ApplicationRepository applicationRepository) {

        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }


    // ================= SIGNUP =================

    @GetMapping("/signup")
    public String signupPage() {
        return "company-signup";
    }

    @PostMapping("/signup")
    public String signup(
            @ModelAttribute Company company,
            Model model) {

        if (companyRepository.findByEmail(company.getEmail()).isPresent()) {
            model.addAttribute("error", "A company with this email already exists!");
            return "company-signup";
        }

        companyRepository.save(company);

        model.addAttribute("message", "Company account created! Please login.");

        return "company-login";
    }


    // ================= LOGIN / LOGOUT =================

    @GetMapping("/login")
    public String loginPage() {
        return "company-login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Company company = companyRepository.findByEmail(email).orElse(null);

        if (company != null && company.getPassword() != null
                && company.getPassword().equals(password)) {

            session.setAttribute("loggedInCompany", company);

            return "redirect:/company/dashboard";
        }

        model.addAttribute("error", "Invalid email or password!");

        return "company-login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.removeAttribute("loggedInCompany");

        return "redirect:/company/login";
    }


    // ================= DASHBOARD =================

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        Company company = (Company) session.getAttribute("loggedInCompany");

        if (company == null) {
            return "redirect:/company/login";
        }

        List<Job> jobs = jobRepository.findByPostedByOrderByPostedDateDesc(company);

        model.addAttribute("company", company);
        model.addAttribute("jobs", jobs);

        return "company-dashboard";
    }


    // ================= CREATE JOB =================

    @GetMapping("/jobs/new")
    public String newJobPage(Model model, HttpSession session) {

        Company company = (Company) session.getAttribute("loggedInCompany");

        if (company == null) {
            return "redirect:/company/login";
        }

        model.addAttribute("company", company);
        model.addAttribute("job", new Job());

        return "company-job-form";
    }

    @PostMapping("/jobs/new")
    public String createJob(
            @ModelAttribute Job job,
            HttpSession session) {

        Company company = (Company) session.getAttribute("loggedInCompany");

        if (company == null) {
            return "redirect:/company/login";
        }

        // Company details always come from the logged-in company account
        job.setPostedBy(company);
        job.setCompany(company.getName());
        job.setCompanyWebsite(company.getWebsite());
        job.setContactEmail(company.getEmail());
        job.setPostedDate(LocalDate.now());
        job.setStatus("Open");

        jobRepository.save(job);

        return "redirect:/company/dashboard";
    }


    // ================= APPLICANTS FOR A JOB =================

    @GetMapping("/jobs/{id}/applicants")
    public String applicants(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        Company company = (Company) session.getAttribute("loggedInCompany");

        if (company == null) {
            return "redirect:/company/login";
        }

        Job job = findOwnJob(id, company);

        if (job == null) {
            return "redirect:/company/dashboard";
        }

        model.addAttribute("company", company);
        model.addAttribute("job", job);
        model.addAttribute(
                "applications",
                applicationRepository.findByJobOrderByAppliedAtDesc(job));

        return "company-applicants";
    }


    // ================= UPDATE STATUS (Shortlist / Reject) =================

    @PostMapping("/applications/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            HttpSession session) {

        Company company = (Company) session.getAttribute("loggedInCompany");

        if (company == null) {
            return "redirect:/company/login";
        }

        Application application = findOwnApplication(id, company);

        if (application == null) {
            return "redirect:/company/dashboard";
        }

        if (status.equals("Applied")
                || status.equals("Shortlisted")
                || status.equals("Rejected")) {

            application.setStatus(status);

            // If a student was already selected and is now changed,
            // remove them from the placement drive
            application.setDriveDate(null);
            application.setDriveTime(null);
            application.setDriveVenue(null);
            application.setDriveNotes(null);

            applicationRepository.save(application);
        }

        return "redirect:/company/jobs/"
                + application.getJob().getId() + "/applicants";
    }


    // ================= SELECT STUDENT + SET DRIVE DATE =================

    @PostMapping("/applications/{id}/select")
    public String selectStudent(
            @PathVariable Long id,
            @RequestParam("driveDate")
            @org.springframework.format.annotation.DateTimeFormat(
                    iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
            LocalDate driveDate,
            @RequestParam(required = false) String driveTime,
            @RequestParam(required = false) String driveVenue,
            @RequestParam(required = false) String driveNotes,
            HttpSession session) {

        Company company = (Company) session.getAttribute("loggedInCompany");

        if (company == null) {
            return "redirect:/company/login";
        }

        Application application = findOwnApplication(id, company);

        if (application == null) {
            return "redirect:/company/dashboard";
        }

        application.setStatus("Selected");
        application.setDriveDate(driveDate);
        application.setDriveTime(driveTime);
        application.setDriveVenue(driveVenue);
        application.setDriveNotes(driveNotes);

        applicationRepository.save(application);

        return "redirect:/company/jobs/"
                + application.getJob().getId() + "/applicants";
    }


    // ================= HELPERS =================

    // Returns the job only if it belongs to this company
    private Job findOwnJob(Long id, Company company) {

        Job job = jobRepository.findById(id).orElse(null);

        if (job == null || job.getPostedBy() == null
                || !job.getPostedBy().getId().equals(company.getId())) {
            return null;
        }

        return job;
    }

    // Returns the application only if its job belongs to this company
    private Application findOwnApplication(Long id, Company company) {

        Application application = applicationRepository.findById(id).orElse(null);

        if (application == null || application.getJob() == null
                || findOwnJob(application.getJob().getId(), company) == null) {
            return null;
        }

        return application;
    }
}
