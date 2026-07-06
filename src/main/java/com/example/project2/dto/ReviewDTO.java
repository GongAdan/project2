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

    // 별점 (1~5)
    private Integer score;

    // 한 줄 리뷰
    private String content;

    // 작성일
    private LocalDateTime createdAt;

    // 수정일
    private LocalDateTime updatedAt;

    // 작성자
    private String username;
    private String nickname;

    // 직업
    private Long jobId;
    private String jobName;
}