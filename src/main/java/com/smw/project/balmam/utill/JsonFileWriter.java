package com.smw.project.balmam.utill;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smw.project.balmam.dto.PathCoordinateDto;

public class JsonFileWriter {

    public static void writeToJsonFile(List<List<PathCoordinateDto>> pathCoordinates, String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 객체를 JSON 파일로 변환
            mapper.writeValue(new File(filePath), pathCoordinates);
            System.out.println("JSON file was created successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("An error occurred while writing the JSON file: " + e.getMessage());
        }
    }
}