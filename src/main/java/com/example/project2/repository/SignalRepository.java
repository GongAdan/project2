package com.example.project2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project2.entity.Signal;

public interface SignalRepository extends JpaRepository<Signal, Long> {

    List<Signal> findByJobJobIdOrderBySlotNumber(Long jobId);

    Optional<Signal> findFirstByJobJobIdAndActiveFalseOrderBySlotNumber(Long jobId);

    long countByJobJobIdAndActiveTrue(Long jobId);

}
