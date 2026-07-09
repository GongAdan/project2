package com.example.project2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project2.common.Tier;
import com.example.project2.entity.Signal;

public interface SignalRepository extends JpaRepository<Signal, Long> {

    // 기존 메서드들은 유지하세요
    List<Signal> findByJobJobIdOrderBySlotNumber(Long jobId);

    // 💡 추가: 티어별 활성 신호기 조회 (방이 열렸는지 체크할 때 사용)
    void deleteBySessionId(String sessionId);

    // 티어별 활성 인원수 확인 (방 열림 체크용)
    long countByTierAndActiveTrue(Tier tier);

    // 특정 티어의 활성 목록 조회
    List<Signal> findByTierAndActiveTrue(Tier tier);

    // 내 세션으로 현재 활성화된 정보 찾기
    Optional<Signal> findBySessionIdAndActiveTrue(String sessionId);

}
