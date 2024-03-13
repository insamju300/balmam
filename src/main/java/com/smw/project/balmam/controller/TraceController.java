package com.smw.project.balmam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.smw.project.balmam.dto.RouteRecordingDTO;

@Controller
public class TraceController {
	//회원가입
	@GetMapping("/trace/routeRecording")
	public String showRouteRecording() {
		return "/trace/routeRecording";
	}
	
	@PostMapping("/trace/routeRecording")
	public String doRouteRecording(@RequestBody RouteRecordingDTO routeRecordingDTO) {
	    System.err.println("레코드 시작 시간: " + routeRecordingDTO.getRecordingStartTime());
	    System.err.println("레코드 종료 시간: " + routeRecordingDTO.getRecordingEndTime());
	    System.err.println("총 일시정지 시간: " + routeRecordingDTO.getTotalPauseTime());
	    System.err.println("경로 좌표 그룹" + routeRecordingDTO.getPathCoordinatesGroups());
	    System.err.println("방문 도시 목록" + routeRecordingDTO.getStayedCities());
	    System.err.println("미디어 목록" + routeRecordingDTO.getGeoMedias());
	    
	    // Processing goes here...
	    return "/trace/routeRecording";
	    //return "redirect:/traceDetail/{traceId}"; // Adjust the redirection as needed
	}

}
