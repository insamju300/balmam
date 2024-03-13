package com.smw.project.balmam.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class PathCoordinateDto extends CoordinateDto{
	private Timestamp time; // 시간

	@Override
	public String toString() {
		return "PathCoordinateDto [time=" + time + ", getLat()=" + getLat() + ", getLng()=" + getLng() + ", toString()="
				+ super.toString() + "]";
	}
	
	
}
