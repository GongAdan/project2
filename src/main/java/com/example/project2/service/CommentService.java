package com.example.project2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project2.dto.CommentDTO;
import com.example.project2.entity.Board;
import com.example.project2.entity.Comment;
import com.example.project2.repository.BoardRepository;
import com.example.project2.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    // Service 계층은 비지니스 로직의 연결을 수행
    // 1개 이상의 DB간 데이터 변경을 수행하고,
    // 이를 트랜잭션으로 처리하는 계층
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 댓글 등록 : 댓글 추가 + 게시글 테이블 수정
    @Transactional
    // 이 메소드 안의 DB 작업들을 하나의 작업 단위로 묶어주는 어노테이션
    // 데이터 무결성을 보장하는 기능
    // 또한, 변경 감지(Dirty Checking) 기능도 수행
    public int save(CommentDTO commentDTO) {

        Board board = boardRepository.findById(commentDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다"));

        Comment comment = Comment.builder()
                .content(commentDTO.getContent())
                .board(board)
                .build();

        commentRepository.save(comment);

        return 1;
    }

    // 특정 게시글의 댓글 전체 조회
    public List<CommentDTO> getCommentsByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardBoardId(boardId);
        return comments.stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Entity -> DTO
    private CommentDTO toDTO(Comment comment) {
    return CommentDTO.builder()
            .commentId(comment.getCommentId())
            .content(comment.getContent())
            .createdAt(comment.getCreatedAt())
            .boardId(comment.getBoard().getBoardId())
            .accountId(comment.getAccount().getAccountId())
            .nickname(comment.getAccount().getNickname())
            .build();
    }

}
