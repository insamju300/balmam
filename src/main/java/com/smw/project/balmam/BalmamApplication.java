package com.smw.project.balmam;

import java.io.File;

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
        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Upload directory created successfully.");
            } else {
                System.err.println("Failed to create upload directory.");
            }
        } else {
            System.out.println("Upload directory already exists.");
        }
    }
}