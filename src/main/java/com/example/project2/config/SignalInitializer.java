package com.example.project2.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.project2.entity.Job;
import com.example.project2.entity.Signal;
import com.example.project2.repository.JobRepository;
import com.example.project2.repository.SignalRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignalInitializer implements CommandLineRunner {

    private final JobRepository jobRepository;
    private final SignalRepository signalRepository;

    @Override
    public void run(String... args) throws Exception {

        // 이미 생성되어 있다면 종료
        if (signalRepository.count() > 0) {
            return;
        }

        for (Job job : jobRepository.findAll()) {

            for (int i = 1; i <= 10; i++) {

                Signal signal = Signal.builder()
                        .job(job)
                        .slotNumber(i)
                        .active(false)
                        .build();

                signalRepository.save(signal);
            }
        }

        System.out.println("Signal 슬롯 생성 완료");
    }
}