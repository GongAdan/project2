package com.example.project2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.project2.common.JobRole;
import com.example.project2.dto.JobDTO;
import com.example.project2.entity.Job;
import com.example.project2.repository.JobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    // 전체 직업 조회
    public List<JobDTO> getJobs() {

        return jobRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 직업 상세 조회
    public JobDTO getJob(Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new IllegalArgumentException("직업이 존재하지 않습니다."));

        return toDTO(job);
    }

    public List<Job> getJobsByRole(JobRole role){

    return jobRepository.findByRoleOrderByDisplayOrder(role);

}

    private JobDTO toDTO(Job job) {

        return JobDTO.builder()
                .jobId(job.getJobId())
                .jobName(job.getJobName())
                .description(job.getDescription())
                .iconPath(job.getIconPath())
                .role(job.getRole())
                .build();
    }
}