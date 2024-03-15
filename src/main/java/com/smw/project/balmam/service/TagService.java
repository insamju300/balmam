package com.smw.project.balmam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smw.project.balmam.entity.TagEntity;
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
	
	
}
