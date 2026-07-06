package com.example.project2.controller;

import com.example.project2.dto.ReviewDTO;
import com.example.project2.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    // JavaScript에서 fetch("/api/review", { method: "POST" })로 보낸 요청을 여기서 받습니다.
    @PostMapping
    public ResponseEntity<?> registerReview(@RequestBody ReviewDTO reviewDTO, Principal principal) {
        try {
            // 현재 로그인한 사용자의 username을 가져옵니다.
            String username = principal.getName();
            
            // 서비스단의 등록 로직 호출
            reviewService.register(reviewDTO, username);
            
            // 성공 시 200 OK 상태코드와 성공 메시지 반환
            return ResponseEntity.ok("리뷰가 등록되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            // 별점 오류(1~5점)나 이미 작성한 리뷰인 경우 400 Bad Request와 에러 메시지 반환
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // 기타 서버 에러 처리
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다.");
        }
    }
}