package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.smw.project.balmam.entity.TagEntity;
import com.smw.project.balmam.enums.RelType;
import com.smw.project.balmam.enums.TagType;

@Mapper
public interface TagRepository {

	@Insert({ "<script>", 
				"INSERT INTO `TAG` (color, `name`, type)", 
				"VALUES",
				"<foreach collection='tags' item='tag' separator=','>", 
				"(#{tag.color}, #{tag.name}, #{tag.type})",
				"</foreach>", 
				"ON DUPLICATE KEY UPDATE name=name",
			  "</script>" })
	public void insertTags(@Param("tags") List<TagEntity> tags);
	
	@Select("""
			    SELECT * FROM `TAG` WHERE `name` = #{tag.name} AND type = #{tag.type}
			""")
	public TagEntity findOneByNameAndType(@Param("tag") TagEntity tag);

	@Insert("""
		INSERT INTO `TAG` (color, `name`, type) 
		VALUES
		(#{color}, #{name}, #{type})
	  """)
	@Options(useGeneratedKeys=true, keyProperty="id")
	public void insertTag(TagEntity tag);

    @Select("SELECT t.* FROM Tag t " +
            "INNER JOIN TagMapping tm ON t.id = tm.tagId " +
            "WHERE tm.relId = #{relId} AND tm.relType = #{relType} AND t.type = #{tagType}")
    List<TagEntity> findTagsByRelInfoAndTagType(@Param("relId") Long relId, 
                                                 @Param("relType") RelType relType, 
                                                 @Param("tagType") TagType tagType);
    

    List<TagEntity> findTagsByRelInfoAndTagTypeForStayedCities(@Param("relId") Long relId, 
            @Param("relType") RelType relType, 
            @Param("tagType") TagType tagType);
    
    
    @Select("SELECT t.* FROM Tag t INNER JOIN (" +
            "SELECT sc.name, sc.traceId, sc.stayedTime FROM StayedCity sc " +
            "WHERE sc.traceId = #{traceId} " +
            "ORDER BY sc.stayedTime DESC LIMIT 3) as topCities " +
            "ON t.name = topCities.name AND t.type = #{tagType} " +
            "ORDER BY topCities.stayedTime DESC")
    List<TagEntity> findTagsByRelInfoAndTagTypeForStayedCities(@Param("traceId") Long traceId, 
                                                               @Param("tagType") TagType tagType);
}