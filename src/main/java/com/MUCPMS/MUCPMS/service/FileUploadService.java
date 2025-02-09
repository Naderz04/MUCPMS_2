package com.MUCPMS.MUCPMS.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    private static final String UPLOAD_DIR = "uploads/";

    public String uploadFile(MultipartFile file, String subDirectory) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String uploadPath = UPLOAD_DIR + subDirectory + "/";
        Path directory = Paths.get(uploadPath);

        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        Path filePath = directory.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return subDirectory + "/" + fileName;
    }
}