package com.example.project2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // 이 클래스가 JPA 엔티티임을 정의
// Board 클래스의 구조에 맞게 DB 테이블이 생성됨
// @Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachId;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileRealName;

    @Column(nullable = false)
    private String fileChgName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
    // @ManyToOne
    // 다대일(N:1) 관계를 의미
    // 여러 개의 첨부파일(Attach)은 하나의 게시글(Board)에 속함

    // FetchType.LAZY
    // 지연 로딩 전략
    // Attach 조회 시 Board 정보를 즉시 조회하지 않고,
    // 실제 Board 객체를 사용하는 시점에 조회 쿼리를 실행

    // @JoinColumn(name="board_id")
    // attach 테이블의 외래키 컬럼명을 지정
    // board_id 컬럼이 board 테이블의 PK를 참조
}
