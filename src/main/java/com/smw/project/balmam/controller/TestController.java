package com.smw.project.balmam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.smw.project.balmam.service.TestService;



@Controller
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public String showTime(Model model) {
        model.addAttribute("currentTime", testService.getCurrentTime().getCurrentTime());
        return "test";
    }
}