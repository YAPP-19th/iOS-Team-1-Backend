package com.yapp.project.aux.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class SnapShotUtil {
    private SnapShotUtil(){}
    public static String saveImages(MultipartFile image, Long id, String filePath) throws IOException {
        int dotIndex = image.getOriginalFilename().lastIndexOf(".");
        String fileName = image.getOriginalFilename().substring(0, dotIndex);
        String extension = image.getOriginalFilename().substring(dotIndex);
        String saveFileName = fileName + "_" + LocalDate.now() + extension;
        final String SAVE_PATH = filePath + id + "/";
        // Todo 추후 S3 전환 시, 아래 5라인 및 경로 수정, FILE_SERVER_PATH도 수정해야함.
        Path directoryPath = Paths.get(SAVE_PATH);
        Files.createDirectories(directoryPath);
        image.transferTo(new File(SAVE_PATH, saveFileName));
        return SAVE_PATH + saveFileName;
    }
}
