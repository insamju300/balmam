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
public class NaverAirportController {
	
	@Autowired
	NaverAirportService airportPickerService;
	
	@GetMapping("/travlePlan/showAirportPicker")
	@ResponseBody
	public String showAirportPicker(Model model) {
		List<NaverAirportEntity> latesNaverAirportEntities = airportPickerService.findLatestNaverAirport();
		
		List<NaverAirportEntity> departureAirportEntities = latesNaverAirportEntities.stream().filter((airport)->airport.getIsDeparture()).toList();
		List<ResponseNaverAirportDto> departureAirportDtos = airportPickerService.airportEntitesToDtos(departureAirportEntities);
		List<NaverAirportEntity> returnAirportEntities = latesNaverAirportEntities.stream().filter((airport)->!airport.getIsDeparture()).toList();
		List<ResponseNaverAirportDto> returnAirportDtos = airportPickerService.airportEntitesToDtos(returnAirportEntities);
		
		model.addAttribute("departureAirports", departureAirportDtos);
		model.addAttribute("returnAirports", returnAirportDtos);
		
		return "/";
	}
	
	
	
	

}
