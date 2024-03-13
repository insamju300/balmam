package com.smw.project.balmam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TraceController {
	//회원가입
	@GetMapping("/trace/routeRecording")
	public String showRouteRecording() {
		return "/trace/routeRecording";
	}

}
