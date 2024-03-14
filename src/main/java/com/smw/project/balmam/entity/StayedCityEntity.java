package com.smw.project.balmam.entity;

import java.security.Timestamp;

import com.smw.project.balmam.dto.StayedCityDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StayedCityEntity {
    private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private Long traceId;
    private Long stayedTime;
    private String name;
    
    public StayedCityEntity(StayedCityDto stayedCityDto, Long traceId) {
		this.traceId =  traceId;
		this.stayedTime =  stayedCityDto.getStayedTime();
		this.name =  stayedCityDto.getName();
	}
}
