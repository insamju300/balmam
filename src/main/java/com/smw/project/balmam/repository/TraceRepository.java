package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.smw.project.balmam.dto.TraceListRequestDto;
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
		        "IF(#{userId} IS NULL, FALSE, (SELECT COUNT(*) FROM `Like` le WHERE le.relId = t.id AND le.memberId = #{userId} AND le.relType = 'Trace') > 0) AS extra__isLiked, " +
		        "IF(#{userId} IS NULL, FALSE, (SELECT COUNT(*) FROM Bookmark be WHERE be.relId = t.id AND be.memberId = #{userId} AND be.relType = 'Trace') > 0) AS extra__isBookmarked, " +
		        "IF(#{userId} IS NULL, FALSE, t.writerId = #{userId}) AS extra__isAccessible " +
		        "FROM trace t " +
		        "JOIN Member m ON t.writerId = m.id " +
		        "LEFT JOIN mediaFiles mf ON m.profileImageId = mf.id " +
		        "WHERE t.id = #{traceId} AND t.isDeleted = FALSE")
	    TraceEntity findTraceByIdAndUserIdForPrintDetial(@Param("traceId") Long traceId, @Param("userId") Long userId);
	    
	    
	    @Select("<script>" +
	            "SELECT " +
	            "   t.*, " +
	            "   m.nickname AS extra__writerNickname, " +
	            "   m.profileImageId AS extra__writerProfileImageId, " +
	            "   mf.name AS extra__writerProfileImageName, " +
	            "   mf2.name AS extra__featuredImageName, " +
	            "   mf2.type AS extra__featuredImageType, " +
	            "   mf2.thumbnailName AS extra__featuredImageThumbnailName " +
	            "FROM trace t " +
	            "LEFT JOIN Member m ON t.writerId = m.id " +
	            "LEFT JOIN mediaFiles mf ON m.profileImageId = mf.id " +
	            "LEFT JOIN mediaFiles mf2 ON t.featuredImageId = mf2.id " +
	            "<if test='tagId != -1'>" +
	            "   LEFT JOIN tagMapping tm ON t.id = tm.relId and tm.relType = 'trace'" +
	            "</if>" +
	            "<if test='!cityName.equals(\"\")'>"+
	               "LEFT JOIN StayedCity sc "+
	               "ON sc.traceId = t.id "+
	            "</if>"+
	            
	            "WHERE t.isDeleted = FALSE AND t.status = 'done' " +
	            "<if test='lastItemTraceId != null and lastItemOrderPoint != null'>" +
	            "   AND (t.orderPoint &lt; #{lastItemOrderPoint} OR (t.orderPoint = #{lastItemOrderPoint} AND t.id &lt; #{lastItemTraceId})) " +
	            "</if>" +
	            "<if test='!cityName.equals(\"\")'>" +
	            "   AND sc.name = #{cityName}" + 
	            "</if>" +
	            "<if test='tagId != -1'>" +
	            "   AND tm.tagId = #{tagId}" + 
	            "</if>" +
	            "ORDER BY t.orderPoint DESC, t.id DESC " +
	            "LIMIT #{limit}" +
	            "</script>")
	    List<TraceEntity> findTracesForPrintList(TraceListRequestDto traceListRequestDto);

	    @Update("""
	    		
	    		UPDATE trace
	    		SET hitCount = hitCount + 1
	    		WHERE id = #{id};
	    		"""
	    		)
		void increaseHitCount(Long id);

	    @Select("""
	    		SELECT hitCount FROM TRACE
	    		WHERE id = #{id};
	    		""")
		Integer getHitCount(Long id);

	    @Update("""
	    		   Update TRACE SET LIKECOUNT = LIKECOUNT+1
	    		   WHERE id = #{id};
	    		""")
		void increaseLikeCount(Long id);

	    @Update("""
	    		   Update TRACE SET LIKECOUNT = LIKECOUNT-1
	    		   WHERE id = #{id};
	    		""")
		void descreaseLikeCount(Long id);

	    @Update("""
	    		   Update TRACE SET BOOKMARKCOUNT = BOOKMARKCOUNT+1
	    		   WHERE id = #{id};
	    		""")
		void increaseBookmarkCount(Long id);

	    @Update("""
	    		   Update TRACE SET BOOKMARKCOUNT = BOOKMARKCOUNT-1
	    		   WHERE id = #{id};
	    		""")
		void descreaseBookmarkCount(Long id);

	    @Update("""
	    		   Update TRACE SET isDeleted = True, deletedDate = NOW()
	    		   WHERE id = #{id};
	    		""")	    
		void updateDeleteFlug(Long id);

	    
	    @Select("""
	    		   SELECT COUNT(id) FROM trace WHERE isDeleted=FALSE AND `status` = 'done' AND writerId = #{writerId}
	    		""")
		int getTraceCountFromMemberId(Long writerId);
	    
	    

	    
	   
	    

}
