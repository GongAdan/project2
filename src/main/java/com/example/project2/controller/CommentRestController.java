package com.example.project2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<List<CommentDTO>> create(
            @RequestBody CommentDTO commentDTO,
            Authentication authentication) { // 💡 현재 로그인한 유저 정보를 인자로 받음

        // 시큐리티 예외 처리 (만약을 위한 이중 방어)
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();

        // 댓글을 저장하고, 저장된 게시글의 '최신 댓글 전체 목록'을 가져옵니다.
        List<CommentDTO> updatedComments = commentService.save(commentDTO, username);

        // 200 OK 상태 코드와 함께 최신 댓글 리스트를 JSON으로 반환
        return ResponseEntity.ok(updatedComments);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<List<CommentDTO>> delete(
            @PathVariable Long commentId,
            @RequestParam Long boardId, // 삭제 후 최신 목록을 그리기 위해 boardId를 받습니다.
            Authentication authentication) {

        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        // 서비스에서 삭제를 처리하고, 해당 글의 최신 댓글 리스트를 받아옴
        List<CommentDTO> updatedComments = commentService.delete(commentId, boardId, authentication.getName());
        return ResponseEntity.ok(updatedComments);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<List<CommentDTO>> update(
            @PathVariable Long commentId,
            @RequestBody CommentDTO commentDTO, // 수정할 내용(content)과 boardId가 담겨옴
            Authentication authentication) {

        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<CommentDTO> updatedComments = commentService.update(commentId, commentDTO, authentication.getName());
        return ResponseEntity.ok(updatedComments);
    }
}
