package com.example.project2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project2.entity.BoardFavorite;

public interface BoardFavoriteRepository
                extends JpaRepository<BoardFavorite, Long> {

        boolean existsByBoardBoardIdAndAccountUsername(
                        Long boardId,
                        String username);

        Optional<BoardFavorite> findByBoardBoardIdAndAccountUsername(
                        Long boardId,
                        String username);

        List<BoardFavorite> findByAccountUsername(String username);

        void deleteByBoardBoardIdAndAccountUsername(
                        Long boardId,
                        String username);

        void deleteByBoardBoardId(Long boardId);

}