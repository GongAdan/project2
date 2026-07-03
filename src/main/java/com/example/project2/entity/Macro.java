package com.example.project2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Macro {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long macroId;

    private Long jobId;       
    private String title;   
    
    @Column(columnDefinition = "TEXT")
    private String content;   
}
