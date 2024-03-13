package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.smw.project.balmam.entity.MediaFileEntity;

@Mapper
public interface FileRepository {
	@Insert("""
			INSERT INTO
			mediaFiles SET
			`name` = #{name},
			thumbnailName = #{thumbnailName},
			size = #{size}, 
			`type` = #{type}
			""")
	@Options(useGeneratedKeys=true, keyProperty="id")
	public void insertMediaFile(MediaFileEntity entity);

	@Select("SELECT * FROM mediaFiles WHERE id=#{id}")
	public MediaFileEntity findFileById(Long id);
}
