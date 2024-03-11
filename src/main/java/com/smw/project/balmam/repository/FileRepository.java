package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import com.smw.project.balmam.entity.MediaFileEntity;

@Mapper
public interface FileRepository {
	@Insert("""
			INSERT INTO
			mediaFile SET
			regDate = NOW(),
			updateDate = NOW(),
			`name` = #{name},
			path = #{path},
			size = #{size}, 
			`type` = #{type}
			""")
	@Options(useGeneratedKeys=true, keyProperty="id")
	public void insertMediaFile(MediaFileEntity entity);
}
