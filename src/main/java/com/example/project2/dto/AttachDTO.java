package com.example.project2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Getter/Setter/toString/equals 등을 자동 생성
@NoArgsConstructor
@AllArgsConstructor
@Builder // 객체 생성 시 데이터 적용을 편리하게 하는 Builder 패턴 제공
public class AttachDTO {
    private Long attachId;
    private String filePath;
    private String fileRealName;
    private String fileChgName;
}
