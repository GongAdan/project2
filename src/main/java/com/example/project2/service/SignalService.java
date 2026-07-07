package com.example.project2.service;

import com.example.project2.repository.JobRepository;
import com.example.project2.repository.SignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.project2.entity.Job;
import com.example.project2.entity.Signal;

import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor // 💡 롬복이 final 필드(Repository 2개)를 자동으로 생성자에 포함시켜 주입해 줍니다.
public class SignalService {

    private final SignalRepository signalRepository;
    private final JobRepository jobRepository;
    
    // 💡 모든 세션(유저)이 공유하는 전역 대기열 맵 (Key: 세션ID 또는 유저ID, Value: 직업ID)
    private final Map<String, Long> matchQueue = new ConcurrentHashMap<>();

    // ✂️ 기존에 직접 작성하셨던 생성자(SignalService(...) { ... })는 롬복과 충돌하므로 깔끔하게 제거했습니다!

    @Transactional
    public void activate(Long jobId, HttpSession session) {
        // 1. 현재 세션에 저장된 signalId가 있는지 확인합니다.
        Long currentSignalId = (Long) session.getAttribute("signalId");
        Signal signal;

        if (currentSignalId != null) {
            // 이미 세션에 존재하면 해당 데이터를 DB에서 찾습니다.
            signal = signalRepository.findById(currentSignalId).orElse(new Signal());
        } else {
            // 처음 참가하는 유저라면 새 객체를 생성합니다.
            signal = new Signal();
        }

        // 2. DB에서 현재 활성화된(active=true) 모든 참가자 목록을 가져옵니다.
        List<Signal> activeList = signalRepository.findByActiveTrue();
        int nextSlot = activeList.size() + 1;

        // 3. 직업 객체를 가져옵니다. (이제 jobRepository가 정상 주입되어 에러가 나지 않습니다!)
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직업입니다."));

        // 4. 엔티티 데이터 세팅
        signal.setActive(true); 
        signal.setSlotNumber(nextSlot);
        signal.setJob(job);

        // 5. DB 저장 및 세션에 ID 저장
        Signal savedSignal = signalRepository.save(signal);
        session.setAttribute("signalId", savedSignal.getSignalId());
    }

    @Transactional
    public void deactivate(HttpSession session) {
        Long currentSignalId = (Long) session.getAttribute("signalId");

        if (currentSignalId != null) {
            // 대기열에서 아예 행을 지워버리는 방식
            signalRepository.deleteById(currentSignalId);

            // 세션에서 신호기 키 제거
            session.removeAttribute("signalId");
        }
    }

    // 💡 현재 전역 대기열에 몇 명이 쌓여있는지 반환 (예: 1/10 계산용)
    public int getQueueCount() {
        return matchQueue.size();
    }

    // 💡 전역 대기열 전체 목록 반환
    public Map<String, Long> getMatchQueue() {
        return matchQueue;
    }

    public List<Signal> findActiveSignals() {
        // DB에서 active가 true인 모든 활성 신호기 목록 조회
        return signalRepository.findByActiveTrue();
    }
}