package com.example.project2.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.project2.common.JobRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId; // 직업번호

    private String jobName;

    private String description;

    private String iconPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobRole role;

    @Column(nullable = false)
    private Integer displayOrder;

    @OneToMany(mappedBy = "job")
    @Builder.Default
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "job")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Signal> signals = new ArrayList<>();
}
