package com.example.project2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.project2.dto.SignalDTO;
import com.example.project2.entity.Job;
import com.example.project2.entity.Signal;
import com.example.project2.repository.JobRepository;
import com.example.project2.repository.SignalRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignalService {

    private final SignalRepository signalRepository;
    private final JobRepository jobRepository;

    // 직업별 신호기 조회
    public List<SignalDTO> getSignals(Long jobId) {

        return signalRepository.findByJobJobIdOrderBySlotNumber(jobId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 신호기 켜기
    @Transactional
    public void activate(Long jobId, HttpSession session) {

        // 이미 켠 상태인지 확인
        if (session.getAttribute("signalId") != null) {
            throw new IllegalStateException("이미 신호기를 켰습니다.");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("직업이 존재하지 않습니다."));

        Signal signal = signalRepository
                .findFirstByJobJobIdAndActiveFalseOrderBySlotNumber(jobId)
                .orElseThrow(() -> new IllegalStateException("빈 신호기가 없습니다."));

        signal.setJob(job);
        signal.setActive(true);

        // 세션에 저장
        session.setAttribute("signalId", signal.getSignalId());
    }

    // 신호기 끄기
    @Transactional
    public void deactivate(HttpSession session) {

        Long signalId = (Long) session.getAttribute("signalId");

        if (signalId == null) {
            return;
        }

        Signal signal = signalRepository.findById(signalId)
                .orElseThrow(() -> new IllegalArgumentException("신호기를 찾을 수 없습니다."));

        signal.setActive(false);

        session.removeAttribute("signalId");
    }

    // 현재 켜져있는 개수
    public long getActiveCount(Long jobId) {

        return signalRepository.countByJobJobIdAndActiveTrue(jobId);
    }

    // Entity → DTO
    private SignalDTO toDTO(Signal signal) {

        return SignalDTO.builder()
                .signalId(signal.getSignalId())
                .jobId(signal.getJob().getJobId())
                .slotNumber(signal.getSlotNumber())
                .active(signal.getActive())
                .build();
    }
}