package com.smw.project.balmam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smw.project.balmam.dto.ResultData;
import com.smw.project.balmam.dto.TagOutputDto;
import com.smw.project.balmam.entity.TagEntity;
import com.smw.project.balmam.enums.TagType;
import com.smw.project.balmam.service.TagService;
import com.smw.project.balmam.utill.Ut;

@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tag/findOrInsertTag")
    public ResultData<TagOutputDto> findOrInsertTag(String tagName) {
    	TagEntity tag = tagService.findOneByNameAndType(new TagEntity(tagName, TagType.commons));
    	
        //없으면 만들어서 넣기. 있으면 가져오기.
    	if (tag == null) {
    		tag = new TagEntity(tagName, Ut.generatePastelColorHex(), TagType.commons);
    		tagService.insertTag(tag);
        	//todo insert실패시 처리 넣기
    		
        }
    	System.err.println(tag.toString());
    	
        return ResultData.ofData("S-1", "태그를 반환합니다", "tag", new TagOutputDto(tag));
        

    }
}