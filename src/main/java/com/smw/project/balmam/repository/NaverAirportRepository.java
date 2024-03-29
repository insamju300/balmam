package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.smw.project.balmam.entity.NaverAirportEntity;

@Mapper
public interface NaverAirportRepository {
    
	@Select("SELECT * FROM NaverAirport WHERE `version` = (SELECT MAX(`version`) FROM NaverAirport) ORDER BY ID")
	public List<NaverAirportEntity> findLatestNaverAirport();
}
