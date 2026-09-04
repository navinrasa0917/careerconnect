package com.careerconnect.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Student who applied
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Job applied for
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    private String resume;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    private String status;

    private LocalDateTime appliedAt;


    // Default Constructor
    public Application() {
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }


    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }


    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }
}