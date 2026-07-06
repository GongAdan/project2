package com.example.project2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project2.entity.Account;
import com.example.project2.entity.Board;
import com.example.project2.entity.BoardLike;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    boolean existsByBoardAndAccount(Board board, Account account);

    Optional<BoardLike> findByBoardAndAccount(Board board, Account account);

    void deleteByBoardAndAccount(Board board, Account account);

    long countByBoard(Board board);

    boolean existsByBoardBoardIdAndAccountUsername(
            Long boardId,
            String username);

    void deleteByBoardBoardId(Long boardId);

}
