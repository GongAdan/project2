package com.example.project2.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project2.entity.Account;
import com.example.project2.entity.Board;
import com.example.project2.entity.BoardLike;
import com.example.project2.repository.AccountRepository;
import com.example.project2.repository.BoardLikeRepository;
import com.example.project2.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;

    /**
     * 추천 토글
     * true : 추천 추가
     * false : 추천 취소
     */
    @Transactional
    public boolean like(Long boardId, String username) {

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 이미 추천한 경우
        if (boardLikeRepository.existsByBoardAndAccount(board, account)) {

            BoardLike boardLike = boardLikeRepository
                    .findByBoardAndAccount(board, account)
                    .orElseThrow();

            boardLikeRepository.delete(boardLike);

            board.setLikeCount(board.getLikeCount() - 1);

            return false;
        }

        // 추천하지 않은 경우
        BoardLike boardLike = BoardLike.builder()
                .account(account)
                .board(board)
                .build();

        boardLikeRepository.save(boardLike);

        board.setLikeCount(board.getLikeCount() + 1);

        return true;
    }

    /**
     * 추천 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean isLiked(Long boardId, String username) {

        Account account = accountRepository.findByUsername(username)
                .orElseThrow();

        Board board = boardRepository.findById(boardId)
                .orElseThrow();

        return boardLikeRepository.existsByBoardAndAccount(board, account);
    }

    @Transactional(readOnly = true)
    public int getLikeCount(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow();

        return board.getLikeCount();

    }

}