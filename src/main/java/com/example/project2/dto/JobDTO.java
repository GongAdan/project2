package com.example.project2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDTO {
    private Long jobId;
    private String jobName;
    private String description;
    private String iconPath;
    private Double averageScore;
    private Long reviewCount;
}
