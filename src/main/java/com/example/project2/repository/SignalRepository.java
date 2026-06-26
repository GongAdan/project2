package com.example.project2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project2.entity.Job;
import com.example.project2.entity.Signal;

public interface SignalRepository extends JpaRepository<Signal, Long> {

    Optional<Signal> findBySessionId(String sessionId);

    boolean existsBySessionId(String sessionId);

    void deleteBySessionId(String sessionId);

    Long countByJob(Job job);
}
