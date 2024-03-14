package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import com.smw.project.balmam.entity.TraceEntity;

@Mapper
public interface TraceRepository {

	@Insert("INSERT INTO Trace (recordingStartTime, recordingEndTime, totalPauseTime, status, writerId) " +
            "VALUES (#{recordingStartTime}, #{recordingEndTime}, #{totalPauseTime}, #{status}, #{writerId})")
	@Options(useGeneratedKeys=true, keyProperty="id")
	void insertTrace(TraceEntity traceEntity);

}
