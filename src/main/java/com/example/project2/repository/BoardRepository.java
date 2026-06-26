package com.example.project2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project2.common.BoardType;
import com.example.project2.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
     Page<Board> findByJobJobIdAndBoardType(
            Long jobId,
            BoardType boardType,
            Pageable pageable);

  
}
