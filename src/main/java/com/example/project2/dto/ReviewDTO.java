package com.example.project2.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long reviewId;
    private Integer score;
    private String content;
    private LocalDateTime createdAt;
    private Long jobId;
    private Long accountId;  
    private String nickname;
}
