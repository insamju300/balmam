package com.smw.project.balmam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smw.project.balmam.dto.LoginInfoDTO;
import com.smw.project.balmam.dto.RelRequestDto;
import com.smw.project.balmam.dto.ResultData;
import com.smw.project.balmam.entity.BookmarkEntity;
import com.smw.project.balmam.entity.LikeEntity;
import com.smw.project.balmam.service.BookmarkService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class BookmarkController {
	
	@Autowired
	BookmarkService bookmarkService;
    
	@PostMapping("/bookmark/increase")
	private ResultData<Integer> bookmarkIncrease(@RequestBody RelRequestDto relTypeDto, HttpServletRequest request){
		LoginInfoDTO loginInfo = (LoginInfoDTO)request.getAttribute("loginInfo");
		//예외처리 넣을 것
		Long memberId = loginInfo.getUserId();
		System.err.println(loginInfo);
		System.err.println("memberId: " + memberId);
		BookmarkEntity bookmarkEntity= new BookmarkEntity(relTypeDto, memberId);

		Integer bookmarkCount = bookmarkService.bookmarkIncrease(bookmarkEntity);
		
		return ResultData.ofData("S-1", "success", "bookmarkCount", bookmarkCount);
	}
	
	@PostMapping("/bookmark/decrease")
	private ResultData<Integer> bookmarkDecrease(@RequestBody RelRequestDto relTypeDto, HttpServletRequest request){
		LoginInfoDTO loginInfo = (LoginInfoDTO)request.getAttribute("loginInfo");
		//예외처리 넣을 것
		Long memberId = loginInfo.getUserId();
		System.err.println(loginInfo);
		BookmarkEntity bookrmarkEntity= new BookmarkEntity(relTypeDto, memberId);
		
		Integer bookmarkCount = bookmarkService.BookmarkDecrease(bookrmarkEntity);
		
		return ResultData.ofData("S-1", "success", "bookmarkCount", bookmarkCount);
	}
}
