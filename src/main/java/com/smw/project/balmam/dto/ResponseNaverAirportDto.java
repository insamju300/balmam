package com.smw.project.balmam.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseNaverAirportDto {
    public String region;
    public List<ResponseAirportDto> airports;
    
}
