package com.smw.project.balmam.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smw.project.balmam.entity.TagEntity;
import com.smw.project.balmam.enums.RelType;
import com.smw.project.balmam.enums.TagType;
import com.smw.project.balmam.repository.TagRepository;
@Service
public class TagService {
	
	@Autowired
	private TagRepository tagRepsoitory;
	
	public void insertTags(List<TagEntity> tags) {
		// TODO Auto-generated method stub
		tagRepsoitory.insertTags(tags);
		
	}
	
	public TagEntity findOneByNameAndType(TagEntity tag) {
		return tagRepsoitory.findOneByNameAndType(tag);
	}

	public void insertTag(TagEntity tag) {
		tagRepsoitory.insertTag(tag);
	}

	public List<TagEntity> findTagsByRelInfoAndTagType(Long relId, RelType relType, TagType tagType) {
		// TODO Auto-generated method stub
		return tagRepsoitory.findTagsByRelInfoAndTagType(relId, relType, tagType);
	}
	
	
	 public List<TagEntity> findTagsByRelInfoAndTagTypeForStayedCities(Long traceId,  TagType tagType){
		 return tagRepsoitory.findTagsByRelInfoAndTagTypeForStayedCities(traceId, tagType);
	 }
	
	
}
