package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.smw.project.balmam.entity.TagEntity;

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

}