package com.example.project2.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.project2.dto.AttachDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${upload.path}")
    private String uploadPath;

    public List<AttachDTO> upload(List<MultipartFile> files){

        List<AttachDTO> result = new ArrayList<>();

        if(files == null || files.isEmpty()){
            return result;
        }

        File dir = new File(uploadPath);

        if(!dir.exists()){
            dir.mkdirs();
        }

        for(MultipartFile file : files){

            if(file.isEmpty()){
                continue;
            }

            String realName = file.getOriginalFilename();

            String chgName =
                    UUID.randomUUID() + "_" + realName;

            try{

                file.transferTo(
                    new File(uploadPath + chgName)
                );

                result.add(
                    AttachDTO.builder()
                        .fileRealName(realName)
                        .fileChgName(chgName)
                        .filePath("/upload/" + chgName)
                        .build()
                );

            }catch(Exception e){
                e.printStackTrace();
            }

        }

        return result;
    }

}