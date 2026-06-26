package com.example.project2.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.example.project2.common.AccountRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // 이 클래스가 JPA 엔티티임을 정의
// Board 클래스의 구조에 맞게 DB 테이블이 생성됨
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId; // 회원 번호
    @Column(nullable = false)
    private String username; // 아이디
    @Column(nullable = false)
    private String password; // 비밀번호
    @Column(nullable = false, unique = true)
    private String nickname; // 닉네임
    @CreationTimestamp
    private LocalDateTime regDate; // 가입 날짜

    @Enumerated(EnumType.STRING)
    // enum 저장할 때 문자열(ROLE_ADMIN 등)으로 DB에 저장
    @Column(nullable = false)
    private AccountRole role;

    @OneToMany(mappedBy = "account")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    @Builder.Default
    private List<PatchNote> patchNotes = new ArrayList<>();

}
