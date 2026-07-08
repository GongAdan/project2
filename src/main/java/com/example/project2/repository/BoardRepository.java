package com.example.project2.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.project2.common.BoardType;
import com.example.project2.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
     Page<Board> findByJobJobIdAndBoardType(
               Long jobId,
               BoardType boardType,
               Pageable pageable);

     @Query("select b from Board b left join fetch b.attachList where b.boardId = :boardId")
     Board findByIdWithAttach(Long boardId);

     @Modifying
     @Transactional
     @Query("""
                   update Board b
                   set b.viewCount = b.viewCount + 1
                   where b.boardId = :boardId
               """)
     int increaseViewCount(@Param("boardId") Long boardId);

     List<Board> findByAccountUsername(String username);
     
}
