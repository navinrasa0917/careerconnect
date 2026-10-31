package com.careerconnect.repository;

import com.careerconnect.entity.Application;
import com.careerconnect.entity.Job;
import com.careerconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByUser(User user);

    // Applications that have NOT been moved to a placement drive yet
    List<Application> findByUserAndDriveDateIsNullOrderByAppliedAtDesc(User user);

    // Applications that were selected and have a drive scheduled
    List<Application> findByUserAndDriveDateIsNotNullOrderByDriveDateAsc(User user);

    boolean existsByUserAndJob(User user, Job job);

    List<Application> findByJobOrderByAppliedAtDesc(Job job);
}