package com.example.project2.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.project2.common.BoardType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {

    private Long boardId;
    private String title;
    private String content;
    private String username;
    private BoardType boardType;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String nickname;
    private Long jobId;
    private String jobName;
    private Integer likeCount;
    private boolean liked;
    private boolean favorited;
    private Integer commentCount;
    private int attachCount;
    private List<AttachDTO> attaches;
    private List<Long> deletedImageIds;
    @Builder.Default
    private List<AttachDTO> attachList = new ArrayList<>();
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

}