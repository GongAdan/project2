package com.example.project2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.project2.dto.ReviewDTO;
import com.example.project2.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("/register")
    public String registerReview(@ModelAttribute ReviewDTO reviewDTO) {

        reviewService.saveReview(reviewDTO);

        // 리뷰 작성 후 해당 직업 페이지로 이동
        return "redirect:/jobs/" + reviewDTO.getJobId();
    }

    // 리뷰 삭제
    @PostMapping("/{reviewId}/delete")
    public String deleteReview(
            @PathVariable Long reviewId,
            @RequestParam Long jobId) {

        reviewService.deleteReview(reviewId);

        return "redirect:/jobs/" + jobId;
    }
}