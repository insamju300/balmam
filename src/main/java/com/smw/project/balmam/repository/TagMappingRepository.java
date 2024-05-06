package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.smw.project.balmam.entity.TagMappingEntity;
import com.smw.project.balmam.enums.RelType;
import com.smw.project.balmam.enums.TagType;

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

	@Delete("""
	DELETE FROM TagMapping WHERE WHERE relId=#{relId}, relType = #{relType}
			
	 """)
	public void deleteMappings(Long relId, RelType relType);

}
