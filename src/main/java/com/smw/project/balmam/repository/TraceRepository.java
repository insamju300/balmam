package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.smw.project.balmam.entity.TraceEntity;

@Mapper
public interface TraceRepository {

	@Insert("INSERT INTO Trace (recordingStartTime, recordingEndTime, totalPauseTime, status, writerId) "
			+ "VALUES (#{recordingStartTime}, #{recordingEndTime}, #{totalPauseTime}, #{status}, #{writerId})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
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



	    @Select("SELECT t.*, " +
		        "m.nickname AS extra__writerNickname, " +
		        "m.profileImageId AS extra__writerProfileImageId, " +
		        "mf.NAME AS extra__writerProfileImageName, " +
		        // userId가 null일 경우 false 반환, 아닐 경우 조건에 맞는지 검사
		        "IF(#{userId} IS NULL, FALSE, (SELECT COUNT(*) FROM LikeEntity le WHERE le.relId = t.id AND le.memberId = #{userId} AND le.relType = 'Trace') > 0) AS extra__isLiked, " +
		        "IF(#{userId} IS NULL, FALSE, (SELECT COUNT(*) FROM BookmarkEntity be WHERE be.relId = t.id AND be.memberId = #{userId} AND be.relType = 'Trace') > 0) AS extra__isBookmarked, " +
		        "IF(#{userId} IS NULL, FALSE, t.writerId = #{userId}) AS extra__isAccessible " +
		        "FROM trace t " +
		        "JOIN Member m ON t.writerId = m.id " +
		        "LEFT JOIN mediaFiles mf ON m.profileImageId = mf.id " +
		        "WHERE t.id = #{traceId} AND t.isDeleted = FALSE")
	    TraceEntity findTraceByIdAndUserIdForPrintDetial(@Param("traceId") Long traceId, @Param("userId") Long userId);
	    
	    

}
