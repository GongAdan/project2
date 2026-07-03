package com.example.project2.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project2.service.BoardLikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    @PostMapping("/like")
    public Map<String, Object> like(
            @RequestParam Long boardId,
            Authentication authentication) {

        Map<String, Object> result = new HashMap<>();

        // 안전장치: 혹시라도 인증 정보가 없으면 에러 메시지나 로그인 필요 상태를 반환
        if (authentication == null || !authentication.isAuthenticated()) {
            result.put("success", false);
            result.put("message", "로그인이 필요한 서비스입니다.");
            return result;
        }

        // 정상 로직 수행
        boolean liked = boardLikeService.like(boardId, authentication.getName());
        int likeCount = boardLikeService.getLikeCount(boardId);

        result.put("success", true);
        result.put("liked", liked);
        result.put("likeCount", likeCount);

        return result;
    }

}
