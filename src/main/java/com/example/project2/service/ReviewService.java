package com.example.project2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project2.dto.ReviewDTO;
import com.example.project2.entity.Account;
import com.example.project2.entity.Job;
import com.example.project2.entity.Review;
import com.example.project2.repository.AccountRepository;
import com.example.project2.repository.JobRepository;
import com.example.project2.repository.ReviewRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
        private final ReviewRepository reviewRepository;
        private final JobRepository jobRepository;
        private final AccountRepository accountRepository;

        @Transactional

        public void deleteReview(Long reviewId) {

                reviewRepository.deleteById(reviewId);
        }

        public void saveReview(ReviewDTO dto) {

                Account account = accountRepository.findById(dto.getAccountId())
                                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

                Job job = jobRepository.findById(dto.getJobId())
                                .orElseThrow(() -> new IllegalArgumentException("직업이 존재하지 않습니다."));

                Review review = Review.builder()
                                .score(dto.getScore())
                                .content(dto.getContent())
                                .account(account)
                                .job(job)
                                .build();

                reviewRepository.save(review);
        }

        public List<ReviewDTO> getReviewsByJob(Long jobId) {

                return reviewRepository.findByJobJobId(jobId)
                                .stream()
                                .map(this::toDTO)
                                .toList();
        }

        public Double getAverageScore(Long jobId) {

                List<Review> reviews = reviewRepository.findByJobJobId(jobId);

                return reviews.stream()
                                .mapToInt(Review::getScore)
                                .average()
                                .orElse(0.0);
        }

        public Long getReviewCount(Long jobId) {

                return (long) reviewRepository.findByJobJobId(jobId)
                                .size();
        }

        private ReviewDTO toDTO(Review review) {

                return ReviewDTO.builder()
                                .reviewId(review.getReviewId())
                                .score(review.getScore())
                                .content(review.getContent())
                                .createdAt(review.getCreatedAt())
                                .accountId(review.getAccount().getAccountId())
                                .nickname(review.getAccount().getNickname())
                                .jobId(review.getJob().getJobId())
                                .build();
        }

}
