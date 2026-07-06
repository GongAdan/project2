package com.example.project2.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.project2.common.BoardType;
import com.example.project2.dto.BoardDTO;
import com.example.project2.service.BoardService;
import com.example.project2.service.JobService;
import com.example.project2.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final BoardService boardService;
    private final ReviewService reviewService;

    @GetMapping("/{jobId}")
    public String detail(@PathVariable Long jobId, Model model) {

        // [기존 코드] 직업 정보 및 게시글 리스트 조회
        model.addAttribute("job", jobService.getJob(jobId));

        model.addAttribute("guideList",
                boardService.getBoardsByJobAndType(
                        jobId,
                        BoardType.GUIDE,
                        PageRequest.of(0, 10)));

        model.addAttribute("macroList",
                boardService.getBoardsByJobAndType(
                        jobId,
                        BoardType.MACRO,
                        PageRequest.of(0, 10)));

        // ========================================================
        // 💡 여기에 리뷰 데이터를 추가합니다! (Thymeleaf 변수 매칭)
        // ========================================================
        
        // 1. 평균 평점 (리뷰가 없으면 Service에서 0.0을 반환)
        model.addAttribute("averageScore", reviewService.getAverageScore(jobId));

        // 2. 총 리뷰 개수
        model.addAttribute("reviewCount", reviewService.getReviewCount(jobId));

        // 3. 리뷰 리스트 (슬라이더용 카드 목록)
        model.addAttribute("reviews", reviewService.getReviews(jobId));

        return "job/guide";
    }

    @GetMapping("/{jobId}/guide/register")
    public String writeGuide(
            @PathVariable Long jobId,
            Model model) {

        model.addAttribute("job",
                jobService.getJob(jobId));

        model.addAttribute("boardDTO",
                new BoardDTO());

        return "board/register";
    }
}