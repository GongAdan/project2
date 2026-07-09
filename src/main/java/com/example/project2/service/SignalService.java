package com.example.project2.service;

import com.example.project2.repository.JobRepository;
import com.example.project2.repository.SignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.project2.common.Tier;
import com.example.project2.entity.Job;
import com.example.project2.entity.Signal;

import java.util.List;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SignalService {

    private final SignalRepository signalRepository;
    private final JobRepository jobRepository;

    @Transactional
    public void activate(Long jobId, String tierStr, HttpSession session) {
        // 1. 세션 기반으로 기존 신호 찾기
        Signal signal = signalRepository.findBySessionIdAndActiveTrue(session.getId())
                .orElse(new Signal());

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID"));

        Tier tier = Tier.valueOf(tierStr);

        // 2. 현재 티어의 활성 인원 확인 후 슬롯 번호 할당 (1 ~ 10)
        long currentCount = signalRepository.countByTierAndActiveTrue(tier);
        int nextSlot = (int) currentCount + 1;

        // 3. 데이터 세팅
        signal.setJob(job);
        signal.setTier(tier);
        signal.setSessionId(session.getId());
        signal.setActive(true);
        signal.setSlotNumber(nextSlot);

        signalRepository.save(signal);
    }

    @Transactional
    public void deactivateBySessionId(String sessionId) {
        signalRepository.deleteBySessionId(sessionId);
    }

    // 💡 누락되었던 티어별 리스트 조회 기능 추가
    public List<Signal> findActiveSignalsByTier(Tier tier) {
        return signalRepository.findByTierAndActiveTrue(tier);
    }

    // 💡 방이 10명 찼는지 확인
    public boolean isRoomFull(Tier tier) {
        return signalRepository.countByTierAndActiveTrue(tier) >= 10;
    }

    public boolean isUserParticipating(String sessionId) {
        // 세션 ID로 활성화된 신호가 있는지 확인하여 true/false 반환
        return signalRepository.findBySessionIdAndActiveTrue(sessionId).isPresent();
    }
}