package com.smw.project.balmam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smw.project.balmam.dto.MessageResponse;

@Controller
public class HomeController {
	@GetMapping("/")
	public String home() {
		return "main";
	}
	

}
