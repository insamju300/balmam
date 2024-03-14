package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.smw.project.balmam.entity.TraceEntity;

@Mapper
public interface TraceRepository {

	@Insert("INSERT INTO Trace (recordingStartTime, recordingEndTime, totalPauseTime, status, writerId) " +
            "VALUES (#{recordingStartTime}, #{recordingEndTime}, #{totalPauseTime}, #{status}, #{writerId})")
	@Options(useGeneratedKeys=true, keyProperty="id")
	void insertTrace(TraceEntity traceEntity);

	@Select("""
			SELECT trace.*, `member`.nickname AS extra__writerNickname
			FROM trace INNER JOIN `member`
			ON trace.writerId = `member`.id
			WHERE trace.id = {id}
			""")
	TraceEntity findTraceByid(Long id);

}
