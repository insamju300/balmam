package com.smw.project.balmam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smw.project.balmam.dto.ResponseNaverAirportDto;
import com.smw.project.balmam.entity.NaverAirportEntity;
import com.smw.project.balmam.service.NaverAirportService;


@Controller
public class CreatePlanController {
	
	@GetMapping("/travlePlan/createPlan")
	public String creatPlan(Model model) {

	}
	

}
