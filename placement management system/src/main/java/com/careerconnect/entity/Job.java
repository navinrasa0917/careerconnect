package com.careerconnect.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Job Information

    private String title;

    private String company;

    private String companyWebsite;

    private String location;

    private String jobType;

    private String workMode;

    private String experience;


    // Salary

    private String salary;

    private String salaryType;

    private String benefits;


    // Job Details

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(columnDefinition = "TEXT")
    private String qualifications;

    @Column(columnDefinition = "TEXT")
    private String preferredSkills;


    // Eligibility

    private String eligibleDepartments;

    private Double minimumCgpa;

    private String backlogsAllowed;

    private String graduationYear;

    private Double minimum10thPercentage;

    private Double minimum12thPercentage;


    // Application Details

    private LocalDate applicationDeadline;

    private LocalDate postedDate;

    private String applicationLink;

    private String contactEmail;


    // Recruitment Details

    private Integer openings;

    @Column(columnDefinition = "TEXT")
    private String recruitmentProcess;

    private String status;


    // Constructor

    public Job() {
        this.postedDate = LocalDate.now();
        this.status = "Open";
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getPreferredSkills() {
        return preferredSkills;
    }

    public void setPreferredSkills(String preferredSkills) {
        this.preferredSkills = preferredSkills;
    }

    public String getEligibleDepartments() {
        return eligibleDepartments;
    }

    public void setEligibleDepartments(String eligibleDepartments) {
        this.eligibleDepartments = eligibleDepartments;
    }

    public Double getMinimumCgpa() {
        return minimumCgpa;
    }

    public void setMinimumCgpa(Double minimumCgpa) {
        this.minimumCgpa = minimumCgpa;
    }

    public String getBacklogsAllowed() {
        return backlogsAllowed;
    }

    public void setBacklogsAllowed(String backlogsAllowed) {
        this.backlogsAllowed = backlogsAllowed;
    }

    public String getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(String graduationYear) {
        this.graduationYear = graduationYear;
    }

    public Double getMinimum10thPercentage() {
        return minimum10thPercentage;
    }

    public void setMinimum10thPercentage(Double minimum10thPercentage) {
        this.minimum10thPercentage = minimum10thPercentage;
    }

    public Double getMinimum12thPercentage() {
        return minimum12thPercentage;
    }

    public void setMinimum12thPercentage(Double minimum12thPercentage) {
        this.minimum12thPercentage = minimum12thPercentage;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getApplicationLink() {
        return applicationLink;
    }

    public void setApplicationLink(String applicationLink) {
        this.applicationLink = applicationLink;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Integer getOpenings() {
        return openings;
    }

    public void setOpenings(Integer openings) {
        this.openings = openings;
    }

    public String getRecruitmentProcess() {
        return recruitmentProcess;
    }

    public void setRecruitmentProcess(String recruitmentProcess) {
        this.recruitmentProcess = recruitmentProcess;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}