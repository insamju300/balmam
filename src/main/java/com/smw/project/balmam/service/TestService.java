package com.smw.project.balmam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smw.project.balmam.entity.Test;
import com.smw.project.balmam.repository.TestRepository;

@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    public Test getCurrentTime() {
        return testRepository.getCurrentTime();
    }
}