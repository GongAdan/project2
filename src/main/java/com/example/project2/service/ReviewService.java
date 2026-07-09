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

    // 🎯 리뷰 수정 기능 추가
    @Transactional
    public void updateReview(Long reviewId, ReviewDTO dto, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        // 권한 검증: 현재 로그인 유저와 작성자 비교
        if (!review.getAccount().getUsername().equals(username)) {
            throw new IllegalStateException("본인이 작성한 리뷰만 수정할 수 있습니다.");
        }

        // 입력 별점 유효성 검증
        if (dto.getScore() < 1 || dto.getScore() > 5) {
            throw new IllegalArgumentException("별점은 1~5점 사이여야 합니다.");
        }

        // 🎯 @Data가 자동으로 만들어준 Setter를 사용하여 필드 업데이트 (변경 감지 작동)
        review.setScore(dto.getScore());
        review.setContent(dto.getContent());
    }

    // 🎯 리뷰 삭제 기능 추가
    @Transactional
    public void deleteReview(Long reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        // 권한 검증: 현재 로그인 유저와 작성자 비교
        if (!review.getAccount().getUsername().equals(username)) {
            throw new IllegalStateException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }

}