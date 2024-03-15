package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.smw.project.balmam.entity.TraceEntity;

@Mapper
public interface TraceRepository {

	@Insert("INSERT INTO Trace (recordingStartTime, recordingEndTime, totalPauseTime, status, writerId) " +
            "VALUES (#{recordingStartTime}, #{recordingEndTime}, #{totalPauseTime}, #{status}, #{writerId})")
	@Options(useGeneratedKeys=true, keyProperty="id")
	void insertTrace(TraceEntity traceEntity);

	@Select("""
			SELECT *
			FROM trace
			WHERE trace.id = #{id}
			""")
	TraceEntity findTraceByid(Long id);

	@Update("""
			<script>
			Update TRACE
			    <set>
		            <if test="title != null">
		                title = #{title},
		            </if>
		            <if test="featuredImageId != null">
		                featuredImageId = #{featuredImageId},
		            </if>
		            <if test="status != null">
		                status = #{status},
		            </if>
		        </set>
    		where id=#{id}
    		</script> 
			""")
	
	
	void updateTrace(TraceEntity traceEntity);

}
