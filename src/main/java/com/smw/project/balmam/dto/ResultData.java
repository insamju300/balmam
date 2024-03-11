package com.smw.project.balmam.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ResultData<T> {
    private String resultCode;
    private String message; // Renamed for clarity
    private T data; // Simplified to a single data field for demonstration
    private String dataName;

    // Private constructor to enforce the use of factory methods
    private ResultData() {}

    // Factory method for a simple message without data
    public static <T> ResultData<T> ofMessage(String resultCode, String message) {
        ResultData<T> result = new ResultData<>();
        result.resultCode = resultCode;
        result.message = message;
        return result;
    }

    // Factory method for a result with data
    public static <T> ResultData<T> ofData(String resultCode, String message, String dataName, T data) {
        ResultData<T> result = new ResultData<>();
        result.resultCode = resultCode;
        result.message = message;
        result.dataName = dataName;
        result.data = data;
        return result;
    }

    // Method to check if the result indicates success
    public boolean isSuccess() {
        return this.resultCode != null && this.resultCode.startsWith("S-");
    }

    // Method to check if the result indicates failure
    public boolean isFailure() {
        return !isSuccess();
    }
}