package com.smw.project.balmam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smw.project.balmam.dto.LoginInfoDTO;
import com.smw.project.balmam.dto.RelRequestDto;
import com.smw.project.balmam.dto.ResultData;
import com.smw.project.balmam.entity.LikeEntity;
import com.smw.project.balmam.service.LikeService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class LikeController {
	
	@Autowired
	LikeService likeService;
    
	@PostMapping("/like/increase")
	private ResultData<Integer> likeIncrease(@RequestBody RelRequestDto relTypeDto, HttpServletRequest request){
		LoginInfoDTO loginInfo = (LoginInfoDTO)request.getAttribute("loginInfo");
		//예외처리 넣을 것
		Long memberId = loginInfo.getUserId();
		System.err.println(loginInfo);
		System.err.println("memberId: " + memberId);
		LikeEntity likeEntity= new LikeEntity(relTypeDto, memberId);
		System.err.println(likeEntity);
		Integer likeCount = likeService.likeIncrease(likeEntity);
		
		return ResultData.ofData("S-1", "success", "likeCount", likeCount);
	}
	
	@PostMapping("/like/decrease")
	private ResultData<Integer> likeDecrease(@RequestBody RelRequestDto relTypeDto, HttpServletRequest request){
		LoginInfoDTO loginInfo = (LoginInfoDTO)request.getAttribute("loginInfo");
		//예외처리 넣을 것
		Long memberId = loginInfo.getUserId();
		System.err.println(loginInfo);
		LikeEntity likeEntity= new LikeEntity(relTypeDto, memberId);
		
		Integer likeCount = likeService.likeDecrease(likeEntity);
		
		return ResultData.ofData("S-1", "success", "likeCount", likeCount);
	}
}
