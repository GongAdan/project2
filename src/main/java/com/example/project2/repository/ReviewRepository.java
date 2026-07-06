package com.example.project2.repository;

import com.example.project2.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // @Param 임포트 필요

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByJobJobId(Long jobId);

    List<Review> findByAccountUsername(String username);

    Optional<Review> findByJobJobIdAndAccountUsername(Long jobId, String username);

    boolean existsByJobJobIdAndAccountUsername(Long jobId, String username);

    // @Param("jobId")를 추가하여 :jobId와 정확히 매치시킵니다.
    @Query("""
            select avg(r.score)
            from Review r
            where r.job.jobId = :jobId
            """)
    Double getAverageScore(@Param("jobId") Long jobId);

    long countByJobJobId(Long jobId);
}