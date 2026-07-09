package com.example.project2.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.project2.dto.ReviewDTO;
import com.example.project2.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

        private final ReviewService reviewService;

        @PostMapping("/register")
        public Map<String, Object> register(
                        @RequestBody ReviewDTO dto,
                        Authentication authentication) {

                reviewService.register(dto, authentication.getName());

                Map<String, Object> result = new HashMap<>();

                result.put("averageScore",
                                reviewService.getAverageScore(dto.getJobId()));

                result.put("reviewCount",
                                reviewService.getReviewCount(dto.getJobId()));

                return result;
        }

        @PutMapping("/{reviewId}")
        public ResponseEntity<String> updateReview(
                        @PathVariable("reviewId") Long reviewId,
                        @RequestBody ReviewDTO dto,
                        Authentication authentication) {
                try {
                        reviewService.updateReview(reviewId, dto, authentication.getName());
                        return ResponseEntity.ok("성공적으로 수정되었습니다.");
                } catch (IllegalArgumentException | IllegalStateException e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                }
        }

        // 🎯 3. 리뷰 삭제 (DELETE)
        @DeleteMapping("/{reviewId}")
        public ResponseEntity<String> deleteReview(
                        @PathVariable("reviewId") Long reviewId,
                        Authentication authentication) {
                try {
                        reviewService.deleteReview(reviewId, authentication.getName());
                        return ResponseEntity.ok("성공적으로 삭제되었습니다.");
                } catch (IllegalArgumentException | IllegalStateException e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                }
        }

}