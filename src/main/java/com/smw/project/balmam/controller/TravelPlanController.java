package com.smw.project.balmam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class TravelPlanController {
	@GetMapping("/travelPlan/create")
	public String createPlan() {
		return "/travelPlan/createPlan";
	}

}
