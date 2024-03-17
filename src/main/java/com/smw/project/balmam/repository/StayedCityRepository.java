package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.smw.project.balmam.entity.StayedCityEntity;
import com.smw.project.balmam.entity.TagEntity;
import com.smw.project.balmam.enums.RelType;
import com.smw.project.balmam.enums.TagType;

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
	public void insertStayedCities(@Param("stayedCities") List<StayedCityEntity> stayedCities);
    
    


    

}
