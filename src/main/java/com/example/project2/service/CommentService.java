package com.example.project2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project2.dto.CommentDTO;
import com.example.project2.entity.Account;
import com.example.project2.entity.Board;
import com.example.project2.entity.Comment;
import com.example.project2.repository.AccountRepository;
import com.example.project2.repository.BoardRepository;
import com.example.project2.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

        private final CommentRepository commentRepository;
        private final BoardRepository boardRepository;
        private final AccountRepository accountRepository; // 💡 Account를 찾기 위해 레포지토리 주입 필요

        // 댓글 등록 : 댓글 추가 후 최신 댓글 목록 반환
        @Transactional
        public List<CommentDTO> save(CommentDTO commentDTO, String username) {

                // 1. 게시글 존재 여부 확인
                Board board = boardRepository.findById(commentDTO.getBoardId())
                                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다"));

                // 🚨 2. 로그인한 작성자 정보 엔티티 조회 (누락 방지)
                Account account = accountRepository.findByUsername(username)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

                // 3. Comment 엔티티 생성 및 연관관계 편의 설정
                Comment comment = Comment.builder()
                                .content(commentDTO.getContent())
                                .board(board)
                                .account(account) // 💡 작성자 바인딩!
                                .build();

                commentRepository.save(comment);

                // 💡 4. 숫자 1 대신, 방금 댓글이 추가된 게시글의 최신 댓글 리스트를 조회해서 반환
                return getCommentsByBoardId(commentDTO.getBoardId());
        }

        // 특정 게시글의 댓글 전체 조회
        public List<CommentDTO> getCommentsByBoardId(Long boardId) {
                List<Comment> comments = commentRepository.findByBoardBoardId(boardId);
                return comments.stream()
                                .map(this::toDTO)
                                .collect(Collectors.toList());
        }

        // 댓글 삭제 로직
        @Transactional
        public List<CommentDTO> delete(Long commentId, Long boardId, String username) {
                Comment comment = commentRepository.findById(commentId)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

                // 🚨 작성자 본인 확인 방어 코드
                if (!comment.getAccount().getUsername().equals(username)) {
                        throw new IllegalStateException("삭제 권한이 없습니다.");
                }

                commentRepository.delete(comment);

                // 삭제 완료 후 최신 댓글 리스트 반환
                return getCommentsByBoardId(boardId);
        }

        // 댓글 수정 로직
        @Transactional
        public List<CommentDTO> update(Long commentId, CommentDTO commentDTO, String username) {
                Comment comment = commentRepository.findById(commentId)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

                // 🚨 작성자 본인 확인 방어 코드
                if (!comment.getAccount().getUsername().equals(username)) {
                        throw new IllegalStateException("수정 권한이 없습니다.");
                }

                comment.setContent(commentDTO.getContent()); 
                                                                

                // 수정 완료 후 최신 댓글 리스트 반환
                return getCommentsByBoardId(commentDTO.getBoardId());
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
                                .username(comment.getAccount().getUsername())
                                .build();
        }
}
