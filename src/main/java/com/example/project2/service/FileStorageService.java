package com.example.project2.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    @Value("${upload.path}")
    private String uploadDir;

    public String save(MultipartFile file) {

        if (file.isEmpty()) {
            return null;
        }

        File dir = new File(uploadDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf("."));

        String savedName = UUID.randomUUID() + ext;

        try {
            file.transferTo(new File(uploadDir, savedName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return savedName;
    }
}
