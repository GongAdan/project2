package com.example.project2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project2.entity.Macro;

import java.util.List;

public interface MacroRepository extends JpaRepository<Macro, Long> {

    // 🎯 특정 직업(jobId)에 속한 매크로 목록을 전체 조회하는 쿼리 메소드
    List<Macro> findByJobId(Long jobId);

}
