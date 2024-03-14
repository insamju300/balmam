package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import com.smw.project.balmam.entity.PathCoordinatesGroupEntity;

public interface PathCoordinatesGroupRepository {

	@Insert("INSERT INTO Trace (traceId, order) " +
            "VALUES (#{traceId}, #{order}")
	@Options(useGeneratedKeys=true, keyProperty="id")
	void insertPathCoordinatesGroupEntity(PathCoordinatesGroupEntity pathCoordinatesGroup);
	
	

}
