package com.smw.project.balmam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PlanController {
	@GetMapping("/plan/create")
	public String createPlan() {
		return "/plan/createPlan";
	}
	

}
