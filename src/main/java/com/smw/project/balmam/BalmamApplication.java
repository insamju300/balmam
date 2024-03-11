package com.smw.project.balmam;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class BalmamApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalmamApplication.class, args);
	}

}

@Component
class FolderInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final String uploadDirectory;

    public FolderInitializer(@Value("${file.upload.realPath}") String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createUploadDirectory();
    }

    private void createUploadDirectory() {
        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) {
            if (uploadDir.mkdirs()) {
                System.out.println("Upload directory created successfully.");
            } else {
                System.err.println("Failed to create upload directory.");
                return;
            }
        } else {
            System.out.println("Upload directory already exists.");
        }

        // 복사할 파일들이 있는 디렉토리의 경로
        Path sourceDirectoryPath = Paths.get("src/main/resources/static/images/avatar");

        // 파일 복사 로직
        try (Stream<Path> paths = Files.walk(sourceDirectoryPath)) {
            paths.filter(Files::isRegularFile).forEach(sourceFilePath -> {
                Path destFilePath = Paths.get(uploadDirectory, sourceFilePath.getFileName().toString());
                if (!Files.exists(destFilePath)) { // 대상 경로에 파일이 없는 경우만 복사
                    try {
                        Files.copy(sourceFilePath, destFilePath);
                        System.out.println(sourceFilePath.getFileName() + " was copied to " + uploadDirectory);
                    } catch (IOException e) {
                        System.err.println("Failed to copy " + sourceFilePath.getFileName());
                    }
                }
            });
        } catch (IOException e) {
            System.err.println("Failed to access " + sourceDirectoryPath);
        }
    }
}