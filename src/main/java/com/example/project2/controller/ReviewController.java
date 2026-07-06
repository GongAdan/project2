package com.example.project2.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.project2.dto.ReviewDTO;
import com.example.project2.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
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

}