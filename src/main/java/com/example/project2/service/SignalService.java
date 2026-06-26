package com.example.project2.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.project2.entity.Job;
import com.example.project2.entity.Signal;
import com.example.project2.repository.JobRepository;
import com.example.project2.repository.SignalRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignalService {

    private final SignalRepository signalRepository;
    private final JobRepository jobRepository;

    @Transactional
    public void signalOn(Long jobId, String sessionId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("직업이 존재하지 않습니다."));

        Optional<Signal> optionalSignal = signalRepository.findBySessionId(sessionId);

        if (optionalSignal.isPresent()) {
            Signal signal = optionalSignal.get();

            // 이미 켜져 있다면 직업만 변경
            signal.setJob(job);

        } else {

            Signal signal = Signal.builder()
                    .sessionId(sessionId)
                    .job(job)
                    .build();

            signalRepository.save(signal);
        }
    }

    @Transactional
    public void signalOff(String sessionId) {

        signalRepository.deleteBySessionId(sessionId);
    }

    public Long getSignalCount(Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("직업이 존재하지 않습니다."));

        return signalRepository.countByJob(job);
    }
}