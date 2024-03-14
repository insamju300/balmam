package com.smw.project.balmam.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.smw.project.balmam.dto.LoginInfoDTO;
import com.smw.project.balmam.dto.PathCoordinateDto;
import com.smw.project.balmam.dto.RouteRecordingDTO;
import com.smw.project.balmam.dto.UserDto;
import com.smw.project.balmam.entity.PathCoordinatesGroupEntity;
import com.smw.project.balmam.entity.TraceEntity;
import com.smw.project.balmam.entity.pathCoordinateEntity;
import com.smw.project.balmam.service.TraceService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TraceController {
	
	@Autowired
	TraceService traceService;
	
	//회원가입
	@GetMapping("/trace/routeRecording")
	public String showRouteRecording() {
		return "/trace/routeRecording";
	}
	
	@PostMapping("/trace/routeRecording")
	public String doRouteRecording(@RequestBody RouteRecordingDTO routeRecordingDTO, HttpServletRequest request) {
		LoginInfoDTO loginInfo = (LoginInfoDTO)request.getAttribute("loginInfo");
		UserDto user = loginInfo.getUserDto();
		
	    System.err.println("레코드 시작 시간: " + routeRecordingDTO.getRecordingStartTime());
	    System.err.println("레코드 종료 시간: " + routeRecordingDTO.getRecordingEndTime());
	    System.err.println("총 일시정지 시간: " + routeRecordingDTO.getTotalPauseTime());
	    System.err.println("경로 좌표 그룹" + routeRecordingDTO.getPathCoordinatesGroups());
	    System.err.println("방문 도시 목록" + routeRecordingDTO.getStayedCities());
	    System.err.println("미디어 목록" + routeRecordingDTO.getGeoMedias());
	    
	    //1단계 trace 테이블 insert
	    TraceEntity traceEntity = new TraceEntity(routeRecordingDTO, user.getId());
	    traceService.insertTrace(traceEntity);
	    
	    //2단계 좌표 그룹 관련 db insert 처리
	    List<List<PathCoordinateDto>> pathCoordinatesGroups = routeRecordingDTO.getPathCoordinatesGroups();
	    //todo 데이터에 대한 예외 처리 할 것.
	    //todo 트랜잭션
	    for(int i = 0; i<pathCoordinatesGroups.size(); i++) {
	    	//그룹을 db에 등록 할 것.
	    	PathCoordinatesGroupEntity pathCoordinatesGroup = new PathCoordinatesGroupEntity(traceEntity.getId(), i);
	    	traceService.insertPathCoordinatesGroup(pathCoordinatesGroup);
	    	final Long pathCoordinatesGroupId =  pathCoordinatesGroup.getId();
	    	
	        List<PathCoordinateDto> pathCoordinateDtos = pathCoordinatesGroups.get(i);
	        
	        PathCoordinateDto test = pathCoordinateDtos.get(0);
	        pathCoordinateEntity entity = new pathCoordinateEntity(test, pathCoordinatesGroupId);
	        
	        List<pathCoordinateEntity> pathCoordinateEntities = pathCoordinateDtos.stream().map(dto -> new pathCoordinateEntity(dto, pathCoordinatesGroupId))
	        		.collect(Collectors.toList());
	        
	        


	    	

	    	//PathCoordinateDto를 pathCoordinateEntity로 변환 
	    	
	    	
	    	
	    	//PathCoordinateDto를 db에 등록할 것
	    }
	    
	    //3단계 방문 도시 관련 db insert 처리
	    
	    //4단계 미디어 목록 관련 db insert 처리
	    
	    
	    // Processing goes here...
	    return "/trace/routeRecording";
	    //return "redirect:/traceDetail/{traceId}"; // Adjust the redirection as needed
	}

}
