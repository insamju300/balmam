package com.smw.project.balmam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@EnableAsync
@SpringBootApplication
public class BalmamApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalmamApplication.class, args);
	}

}

@Component
class FolderInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final String uploadDirectory;
    private final String jsonFilesDirectory;

    public FolderInitializer(@Value("${file.upload.realPath}") String uploadDirectory,
                             @Value("${file.trace.jsonFiles}") String jsonFilesDirectory) {
        this.uploadDirectory = uploadDirectory;
        this.jsonFilesDirectory = jsonFilesDirectory;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createDirectory(uploadDirectory);
        createDirectory(jsonFilesDirectory);
        copyStaticResources();
    }

    private void createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println(directoryPath + " directory created successfully.");
            } else {
                System.err.println("Failed to create " + directoryPath + " directory.");
                return;
            }
        } else {
            System.out.println(directoryPath + " directory already exists.");
        }
    }

    private void copyStaticResources() {
        // 복사할 파일들이 있는 디렉토리의 경로
        List<Path> sourceDirectoryPaths = new ArrayList<>();
        sourceDirectoryPaths.add(Paths.get("src/main/resources/static/images/avatar"));
        sourceDirectoryPaths.add(Paths.get("src/main/resources/static/images/default"));

        for (Path sourceDirectoryPath : sourceDirectoryPaths) {
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
}
