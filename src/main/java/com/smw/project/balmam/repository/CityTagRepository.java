package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.smw.project.balmam.entity.CityTagEntity;

@Mapper
public interface CityTagRepository {

	@Insert({ "<script>", 
				"INSERT INTO CityTag (color, `name`)", 
				"VALUES",
				"<foreach collection='cityTags' item='cityTag' separator=','>", 
				"(#{cityTag.color}, #{cityTag.name})",
				"</foreach>", 
				"ON DUPLICATE KEY UPDATE name=name",
			"</script>" })
	public void insertCityTags(@Param("cityTags") List<CityTagEntity> cityTags);

}