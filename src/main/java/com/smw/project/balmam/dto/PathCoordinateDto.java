package com.smw.project.balmam.dto;

import java.sql.Timestamp;

import com.smw.project.balmam.entity.PathCoordinateEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PathCoordinateDto extends CoordinateDto{
	private Timestamp time; // 시간
	
	

	@Override
	public String toString() {
		return "PathCoordinateDto [time=" + time + ", getLat()=" + getLat() + ", getLng()=" + getLng() + ", toString()="
				+ super.toString() + "]";
	}
	
	
}
