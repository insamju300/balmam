package com.smw.project.balmam.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smw.project.balmam.dto.ResponseAirportDto;
import com.smw.project.balmam.dto.ResponseNaverAirportDto;
import com.smw.project.balmam.entity.NaverAirportEntity;
import com.smw.project.balmam.repository.NaverAirportRepository;


@Service
public class NaverAirportService {
	@Autowired
	NaverAirportRepository naverAirportRepository;
	
	public List<NaverAirportEntity> findLatestNaverAirport(){
		return naverAirportRepository.findLatestNaverAirport();
	}
	
	
	public List<ResponseNaverAirportDto> airportEntitesToDtos(List<NaverAirportEntity> naverAirportEntities){
		String currentRegion="";
		List<ResponseNaverAirportDto> airportDtos = new ArrayList<>();
		
		List<ResponseAirportDto> currentResponseNaverAirport = null;
		
		for(NaverAirportEntity entity: naverAirportEntities) {
			if(!currentRegion.equals(entity.getRegion())) {
				currentRegion = entity.getRegion();
				List<ResponseAirportDto> responseAirportDtos = new ArrayList<>();
				ResponseNaverAirportDto responseNaverAirport = new  ResponseNaverAirportDto(currentRegion, responseAirportDtos);
				airportDtos.add(responseNaverAirport);
				currentResponseNaverAirport = responseAirportDtos;
			}
			
			currentResponseNaverAirport.add(new ResponseAirportDto(entity.getCity(), entity.getCountry(), entity.getCode()));
		}
		
		return airportDtos;
	}

}
