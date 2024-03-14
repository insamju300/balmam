package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import com.smw.project.balmam.entity.GeoMediaEntity;

@Mapper
public interface GeoMediaRepository {

	@Insert("INSERT INTO GeoMedia (lat , lng, traceId) " +
            "VALUES (#{lat}, #{lng}, #{traceId})")
	@Options(useGeneratedKeys=true, keyProperty="id")
	void insertGeoMedia(GeoMediaEntity geoMedia);


}
