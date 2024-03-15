package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.smw.project.balmam.entity.TagMappingEntity;

@Mapper
public interface TagMappingRepository {

	@Insert("""
			<script>
			INSERT INTO TagMapping (relId, relType, tagId) VALUES 
			<foreach collection='tagMappings' item='tagMapping' separator=','>
			    (#{tagMapping.relId}, #{tagMapping.relType}, #{tagMapping.tagId})
			</foreach>
			</script>
			""")
	public void insertTagMappings(@Param("tagMappings")List<TagMappingEntity> tagMappings);

}
