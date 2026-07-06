package com.example.project2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project2.dto.ReviewDTO;
import com.example.project2.entity.Account;
import com.example.project2.entity.Job;
import com.example.project2.entity.Review;
import com.example.project2.repository.AccountRepository;
import com.example.project2.repository.JobRepository;
import com.example.project2.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AccountRepository accountRepository;
    private final JobRepository jobRepository;
    

    // 리뷰 등록
    public void register(ReviewDTO dto, String username) {

        if (dto.getScore() < 1 || dto.getScore() > 5) {
            throw new IllegalArgumentException("별점은 1~5점입니다.");
        }

        if (reviewRepository.existsByJobJobIdAndAccountUsername(
                dto.getJobId(),
                username)) {

            throw new IllegalStateException("이미 리뷰를 작성했습니다.");
        }

        Account account = accountRepository
                .findByUsername(username)
                .orElseThrow();

        Job job = jobRepository
                .findById(dto.getJobId())
                .orElseThrow();

        Review review = Review.builder()
                .score(dto.getScore())
                .content(dto.getContent())
                .account(account)
                .job(job)
                .build();

        reviewRepository.save(review);
    }

    // 직업별 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviews(Long jobId) {

        return reviewRepository.findByJobJobId(jobId)
                .stream()
                .map(review -> ReviewDTO.builder()
                        .reviewId(review.getReviewId())
                        .score(review.getScore())
                        .content(review.getContent())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        .username(review.getAccount().getUsername())
                        .nickname(review.getAccount().getNickname())
                        .jobId(review.getJob().getJobId())
                        .jobName(review.getJob().getJobName())
                        .build())
                .collect(Collectors.toList());

    }

    // 평균 별점
    @Transactional(readOnly = true)
    public double getAverageScore(Long jobId) {

        Double avg = reviewRepository.getAverageScore(jobId);

        return avg == null ? 0.0 : avg;
    }

    // 리뷰 개수
    @Transactional(readOnly = true)
    public long getReviewCount(Long jobId) {

        return reviewRepository.countByJobJobId(jobId);

    }

}