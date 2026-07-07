package com.example.project2.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project2.common.JobRole;
import com.example.project2.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByRoleOrderByDisplayOrder(JobRole role);

    List<Job> findByRole(JobRole role);
    
}
