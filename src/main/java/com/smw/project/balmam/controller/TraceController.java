package com.smw.project.balmam.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.smw.project.balmam.dto.GeoMediasDto;
import com.smw.project.balmam.dto.LoginInfoDTO;
import com.smw.project.balmam.dto.PathCoordinateDto;
import com.smw.project.balmam.dto.RouteRecordingDTO;
import com.smw.project.balmam.dto.StayedCityDto;
import com.smw.project.balmam.dto.UserDto;
import com.smw.project.balmam.entity.CityTagEntity;
import com.smw.project.balmam.entity.GeoMediaEntity;
import com.smw.project.balmam.entity.GeoMediaFileEntity;
import com.smw.project.balmam.entity.MediaFileEntity;
import com.smw.project.balmam.entity.PathCoordinatesGroupEntity;
import com.smw.project.balmam.entity.StayedCityEntity;
import com.smw.project.balmam.entity.TraceEntity;
import com.smw.project.balmam.entity.pathCoordinateEntity;
import com.smw.project.balmam.service.TraceService;
import com.smw.project.balmam.utill.Ut;

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
	    Long traceId = traceEntity.getId();
	    
	    //2단계 좌표 그룹 관련 db insert 처리
	    List<List<PathCoordinateDto>> pathCoordinatesGroups = routeRecordingDTO.getPathCoordinatesGroups();
	    //todo 데이터에 대한 예외 처리 할 것. 적어도 insert db 넘기기 전에 nullcheck정도는 해줄 것
	    //todo 트랜잭션
	    for(int i = 0; i<pathCoordinatesGroups.size(); i++) {
	    	//그룹을 db에 등록 할 것.
	    	PathCoordinatesGroupEntity pathCoordinatesGroup = new PathCoordinatesGroupEntity(traceId, i);
	    	traceService.insertPathCoordinatesGroup(pathCoordinatesGroup);
	    	final Long pathCoordinatesGroupId =  pathCoordinatesGroup.getId();
	    	
	        List<PathCoordinateDto> pathCoordinateDtos = pathCoordinatesGroups.get(i);
	        
	        
	        List<pathCoordinateEntity> pathCoordinateEntities = 
	        		pathCoordinateDtos.stream().map(dto -> new pathCoordinateEntity(dto, pathCoordinatesGroupId)).collect(Collectors.toList());
	        
	        traceService.insertPathCoordinates(pathCoordinateEntities);
	        
	    }
	    
	    //3단계 방문 도시 관련 db insert 처리
	    List<StayedCityDto> stayedCitiesDto = routeRecordingDTO.getStayedCities();
	    List<StayedCityEntity> stayedCities = 
	    		stayedCitiesDto.stream().map(dto->new StayedCityEntity(dto, traceId)).collect(Collectors.toList());
	    
	    traceService.insertStayedCities(stayedCities);
	    
	    List<CityTagEntity> cityTags = 
	    		stayedCitiesDto.stream().map(dto->new CityTagEntity(dto.getName(), Ut.generatePastelColorHex())).collect(Collectors.toList());
	    traceService.insertCityTags(cityTags);	    
	    
	    
	    //4단계 미디어 목록 관련 db insert 처리
	    List<GeoMediasDto> geoMediaDtos = routeRecordingDTO.getGeoMedias();
	    for(GeoMediasDto geoMediaDto : geoMediaDtos) {
	    	GeoMediaEntity geoMedia = new GeoMediaEntity(geoMediaDto.getCoordinate(), traceId);
	    	traceService.insertGeoMedia(geoMedia);
	    	List<GeoMediaFileEntity> geoMediaFiles = geoMediaDto.getMediaFiles().stream().map(files-> new GeoMediaFileEntity(geoMedia.getId(), files.getId())).collect(Collectors.toList());
	        traceService.insertGeoMediaFiles(geoMediaFiles);
	    	
	    }
	    
	    
	    
	    
	    // Processing goes here...
	    return "redirect:/trace/writeTraceDetail?id"+traceId;
	    //return "redirect:/traceDetail/{traceId}"; // Adjust the redirection as needed
	}
	
	@GetMapping("/trace/writeTraceDetail")
	public String showWriteTraceDetail(Long id, Model model) {
		//1. trace 가지고 오기
		TraceEntity traceEntity = traceService.findTraceById(id); 
		
		//todo 예외처리 해주기. 작성 권한등 확인.
		
		//2. 모든 이미지 들고오기.
		List<MediaFileEntity> mediaFileEntity =  traceService.findAllMedaFileByTraceIdFromGeoMedia(traceId);
//		SELECT geoMediaFile.* FROM geoMedia
//		INNER JOIN geoMediaFile 
//		ON geoMedia.id = geoMediaFile.geoMediaId AND geoMedia.isDeleted = FALSE AND geoMediaFile.isDeleted = FALSE
//		INNER JOIN mediaFiles ON geoMediaFile.mediaFileId = 
		
	    //3. dto만들어서 뿌려주기
				
		return "/trace/writeTraceDetail";
	}

}
