package com.smw.project.balmam.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.smw.project.balmam.entity.PathCoordinateEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PathCoordinateOutputDto{
	private Timestamp time; // 시간
	private BigDecimal lat; // 위도
	private BigDecimal lng; // 경도
	
	
	

	@Override
	public String toString() {
		return "PathCoordinateDto [time=" + time + ", getLat()=" + getLat() + ", getLng()=" + getLng() + ", toString()="
				+ super.toString() + "]";
	}




	public PathCoordinateOutputDto(PathCoordinateEntity entity) {
		this.time = entity.getTime();
		this.lat = entity.getLat();
		this.lng = entity.getLng();
	}
	
	
}
