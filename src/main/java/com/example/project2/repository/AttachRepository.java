package com.example.project2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.project2.entity.Attach;

public interface AttachRepository extends JpaRepository<Attach, Long> {
    
    
}
