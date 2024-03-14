package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.smw.project.balmam.entity.StayedCityEntity;

@Mapper
public interface StayedCityRepository {

    @Insert({
        "<script>",
        "INSERT INTO StayedCity (traceId, stayedTime, `name`) VALUES ",
        "<foreach collection='stayedCities' item='stayedCity' separator=','>",
            "(#{stayedCity.traceId}, #{stayedCity.stayedTime}, #{stayedCity.name})",
        "</foreach>",
        "</script>"
    })
    @Options(useGeneratedKeys=true, keyProperty="id")
	public void insertStayedCities(@Param("stayedCities") List<StayedCityEntity> stayedCities);
    
    

}
