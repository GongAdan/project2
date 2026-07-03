package com.example.project2.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    // 추천한 회원
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // 추천한 게시글
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    // 추천한 시간
    @CreationTimestamp
    private LocalDateTime createdAt;

}