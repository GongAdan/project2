package com.example.project2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project2.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBoardBoardId(Long boardId);

    void deleteByBoardBoardId(Long boardId);

}