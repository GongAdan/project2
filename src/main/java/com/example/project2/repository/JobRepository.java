package com.example.project2.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project2.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
    
}
