package com.example.project2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project2.entity.Job;
import com.example.project2.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByJob(Job job);

    List<Review> findByJobJobId(Long jobId);

    Long countByJob(Job job);

}
