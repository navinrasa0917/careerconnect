package com.careerconnect.repository;

import com.careerconnect.entity.Company;
import com.careerconnect.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByPostedByOrderByPostedDateDesc(Company postedBy);
}