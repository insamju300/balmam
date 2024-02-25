package com.smw.project.balmam.entity;

import java.time.LocalDateTime;

public class Test {
    private LocalDateTime currentTime;

    public Test() {}

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }
}