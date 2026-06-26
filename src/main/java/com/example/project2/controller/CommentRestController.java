package com.example.project2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project2.dto.CommentDTO;
import com.example.project2.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentRestController {
    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("")
    public ResponseEntity<Integer> create(
        @RequestBody CommentDTO commentDTO) {
        // @RequestBody : JSON 형태로 클라이언트에서 전송된 데이터를
        // Java 객체로 바인딩 해주는 역할
        int result = commentService.save(commentDTO);
        // ResponseEntity<T> : 상태 코드, 헤더, 데이터를 직접 제어하는 객체
        // 특정 상태 메세지(200, 201 등)을 전송할 때 사용
        // T는 전송할 데이터 타입을 선언
        return ResponseEntity.ok(result);
    }
}
