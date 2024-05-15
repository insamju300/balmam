package com.smw.project.balmam.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smw.project.balmam.dto.CoordinateDto;
import com.smw.project.balmam.dto.GeoMediasDto;
import com.smw.project.balmam.dto.LoginInfoDTO;
import com.smw.project.balmam.dto.MediaFileDto;
import com.smw.project.balmam.dto.MessageResponse;
import com.smw.project.balmam.dto.PathCoordinateDto;
import com.smw.project.balmam.dto.PathCoordinateOutputDto;
import com.smw.project.balmam.dto.ResultData;
import com.smw.project.balmam.dto.RouteRecordingDTO;
import com.smw.project.balmam.dto.StayedCityDto;
import com.smw.project.balmam.dto.TagOutputDto;
import com.smw.project.balmam.dto.TraceListOutputDto;
import com.smw.project.balmam.dto.TraceListRequestDto;
import com.smw.project.balmam.dto.TraceOutputDto;
import com.smw.project.balmam.dto.UserDto;
import com.smw.project.balmam.dto.WriteOrModifyTraceDetailDto;
import com.smw.project.balmam.entity.GeoMediaEntity;
import com.smw.project.balmam.entity.GeoMediaFileEntity;
import com.smw.project.balmam.entity.MediaFileEntity;
import com.smw.project.balmam.entity.PathCoordinateEntity;
import com.smw.project.balmam.entity.PathCoordinatesGroupEntity;
import com.smw.project.balmam.entity.StayedCityEntity;
import com.smw.project.balmam.entity.TagEntity;
import com.smw.project.balmam.entity.TagMappingEntity;
import com.smw.project.balmam.entity.TraceEntity;
import com.smw.project.balmam.enums.RelType;
import com.smw.project.balmam.enums.TagType;
import com.smw.project.balmam.enums.TraceStatus;
import com.smw.project.balmam.service.TagService;
import com.smw.project.balmam.service.TraceService;
import com.smw.project.balmam.utill.Ut;

import jakarta.servlet.http.HttpServletRequest;

//todo ajax용 로딩화면 만들기
@Controller
public class TraceController {

	@Autowired
	TraceService traceService;

	@Autowired
	TagService tagService;
 
	@GetMapping("/trace/routeRecording")
	public String showRouteRecording() {
		return "/trace/routeRecording";
	}

	@Value("${file.upload.path}")
	private String path;
	
	@Value("${file.trace.jsonFiles}")
	private String jsonFileFath;

	@PostMapping("/trace/routeRecording")
	@ResponseBody
	public ResultData<String> doRouteRecording(@RequestBody RouteRecordingDTO routeRecordingDTO,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		LoginInfoDTO loginInfo = (LoginInfoDTO) request.getAttribute("loginInfo");
		UserDto user = loginInfo.getUserDto();

		System.err.println("레코드 시작 시간: " + routeRecordingDTO.getRecordingStartTime());
		System.err.println("레코드 종료 시간: " + routeRecordingDTO.getRecordingEndTime());
		System.err.println("총 일시정지 시간: " + routeRecordingDTO.getTotalPauseTime());
		System.err.println("경로 좌표 그룹" + routeRecordingDTO.getPathCoordinatesGroups());
		System.err.println("방문 도시 목록" + routeRecordingDTO.getStayedCities());
		System.err.println("미디어 목록" + routeRecordingDTO.getGeoMedias());

		// 1단계 trace 테이블 insert
		TraceEntity traceEntity = new TraceEntity(routeRecordingDTO, user.getId());
		traceService.insertTrace(traceEntity);
		Long traceId = traceEntity.getId();
		
		

		// 2단계 좌표 그룹 관련 db insert 처리
		List<List<PathCoordinateDto>> pathCoordinatesGroups = routeRecordingDTO.getPathCoordinatesGroups();
		// todo 데이터에 대한 예외 처리 할 것. 적어도 insert db 넘기기 전에 nullcheck정도는 해줄 것
		// todo 트랜잭션
		for (int i = 0; i < pathCoordinatesGroups.size(); i++) {
			// 그룹을 db에 등록 할 것.
			PathCoordinatesGroupEntity pathCoordinatesGroup = new PathCoordinatesGroupEntity(traceId, i);
			traceService.insertPathCoordinatesGroup(pathCoordinatesGroup);
			final Long pathCoordinatesGroupId = pathCoordinatesGroup.getId();

			List<PathCoordinateDto> pathCoordinateDtos = pathCoordinatesGroups.get(i);

			List<PathCoordinateEntity> pathCoordinateEntities = pathCoordinateDtos.stream()
					.map(dto -> new PathCoordinateEntity(dto, pathCoordinatesGroupId)).collect(Collectors.toList());

			traceService.insertPathCoordinates(pathCoordinateEntities);

		}
		
		traceService.writeToJsonFile(pathCoordinatesGroups, traceId);

		// 3단계 방문 도시 관련 db insert 처리
		List<StayedCityDto> stayedCitiesDto = routeRecordingDTO.getStayedCities();
		List<StayedCityEntity> stayedCities = stayedCitiesDto.stream().map(dto -> new StayedCityEntity(dto, traceId))
				.collect(Collectors.toList());

		traceService.insertStayedCities(stayedCities);

		List<TagEntity> cityTags = stayedCitiesDto.stream()
				.map(dto -> new TagEntity(dto.getName(), Ut.generatePastelColorHex(), TagType.city))
				.collect(Collectors.toList());
		tagService.insertTags(cityTags);

		// 4단계 미디어 목록 관련 db insert 처리
		List<GeoMediasDto> geoMediaDtos = routeRecordingDTO.getGeoMedias();
		for (GeoMediasDto geoMediaDto : geoMediaDtos) {
			GeoMediaEntity geoMedia = new GeoMediaEntity(geoMediaDto.getCoordinate(), traceId);
			traceService.insertGeoMedia(geoMedia);
			List<GeoMediaFileEntity> geoMediaFiles = geoMediaDto.getMediaFiles().stream()
					.map(files -> new GeoMediaFileEntity(geoMedia.getId(), files.getId())).collect(Collectors.toList());
			traceService.insertGeoMediaFiles(geoMediaFiles);

		}

		String redirectUrl = "/trace/writeTraceDetail?id=" + traceId; // 원하는 리다이렉트 경로
		return ResultData.ofData("S-1", "Success", "redirectUrl", redirectUrl);
	}

	@GetMapping("/trace/writeTraceDetail")
	public String showWriteTraceDetail(Long id, Model model) {
		// 1. trace 가지고 오기
		TraceEntity traceEntity = traceService.findTraceById(id);

		// todo 예외처리 해주기. 작성 권한등 확인.

		// 2. 모든 이미지 들고오기.
		List<MediaFileEntity> mediaFileEntitys = traceService.findAllMedaFileByTraceIdFromGeoMedia(id);

		// 3. dto만들어서 뿌려주기
		List<MediaFileDto> mediaFileDtos = mediaFileEntitys.stream().map(entity -> new MediaFileDto(entity, path))
				.toList();

		model.addAttribute("mediaFiles", mediaFileDtos);
		model.addAttribute("traceId", id);

		return "/trace/writeTraceDetail";
	}

	@PostMapping("/trace/writeTraceDetail")
	@ResponseBody
	public ResultData<String> doWriteTraceDetail(@RequestBody WriteOrModifyTraceDetailDto writeOrModifyTraceDto) {
		System.err.println(writeOrModifyTraceDto.toString());

		Long traceId = writeOrModifyTraceDto.getId();
		TraceEntity traceEntity = traceService.findTraceById(traceId);
		// 예외처리 todo

		// trace 테이블 갱신
		String title = writeOrModifyTraceDto.getTitle().trim();
		traceEntity.setTitle(title);

		Long featuredImageId = writeOrModifyTraceDto.getFeaturedImageId();
		traceEntity.setFeaturedImageId(featuredImageId);
		traceEntity.setStatus(TraceStatus.done);

		traceService.updateTrace(traceEntity);

		// 삭제처리된 이미지들 갱신
		List<Long> deletedMediaFileIds = writeOrModifyTraceDto.getDeletedMediaFileIds();
		if (deletedMediaFileIds != null && deletedMediaFileIds.size() > 0) {
			traceService.markMediaFilesAsDeleted(deletedMediaFileIds);
			traceService.updateGeoMediaDeletionStatusForAllDeletedMediaFiles(writeOrModifyTraceDto.getId());
		}

		List<TagOutputDto> tags = writeOrModifyTraceDto.getTags();
		if(tags!=null && tags.size()>0) {
			System.err.println("tags.size() : " + tags.size());
			List<TagMappingEntity> tagMappings = tags.stream()
					.map(tag -> new TagMappingEntity(traceId, RelType.trace, tag.getId())).toList();
			traceService.insertTagMappings(tagMappings);
		}

		String redirectUrl = "/trace/traceDetail?id=" + writeOrModifyTraceDto.getId(); // 원하는 리다이렉트 경로
		System.err.println(redirectUrl);
		return ResultData.ofData("S-1", "Success", "redirectUrl", redirectUrl);
	}

	@GetMapping("/trace/traceDetail")
	public String showTraceDetail(Long id, Model model, HttpServletRequest request) {
		LoginInfoDTO loginInfo = (LoginInfoDTO) request.getAttribute("loginInfo");
		Long userId = null;
		System.err.println(loginInfo);
		if (loginInfo.isLogined()) {
			UserDto user = loginInfo.getUserDto();
			userId = user.getId();
		}
		

		TraceEntity traceEntity = traceService.findTraceByIdAndUserIdForPrintDetial(id, userId);

		// todo trace가 null인경우 처리

		TraceOutputDto trace = new TraceOutputDto(traceEntity, path);

		List<PathCoordinateEntity> pathCoordinateEntities = traceService
				.findPathCoordinatesByTraceIdInPathCoordinatesGroup(id);

		List<List<PathCoordinateOutputDto>> pathCoordinates = new ArrayList<>();
		List<PathCoordinateOutputDto> currentGroup = null;
		Long currentGroupId = null;

		for (PathCoordinateEntity entity : pathCoordinateEntities) {
			Long groupId = entity.getPathCoordinatesGroupId();

			// 현재 그룹 ID가 변경되었는지 확인합니다. 변경되었거나, 첫 번째 요소인 경우 새로운 그룹을 시작합니다.
			if (!groupId.equals(currentGroupId)) {
				currentGroup = new ArrayList<>();
				pathCoordinates.add(currentGroup);
				currentGroupId = groupId;
			}

			// 현재 그룹에 엔티티를 추가합니다.
			if (currentGroup != null) {
				currentGroup.add(new PathCoordinateOutputDto(entity));
			}
		}

		// 좌표 이미지들
		List<MediaFileEntity> mediaFileEntities = traceService.findGeoMedaFilesByTraceId(id);
		List<MediaFileDto> allMediaFiles = new ArrayList<>();

		Map<CoordinateDto, List<MediaFileDto>> groupedByCoordinates = new HashMap<>();

		for (MediaFileEntity entity : mediaFileEntities) {
			CoordinateDto coordinate = new CoordinateDto(entity);
			MediaFileDto mediaFileDto = new MediaFileDto(entity, path);
			allMediaFiles.add(mediaFileDto);

			groupedByCoordinates.computeIfAbsent(coordinate, k -> new ArrayList<>()).add(mediaFileDto);
		}

		List<GeoMediasDto> geoMedias = groupedByCoordinates.entrySet().stream()
				.map(entry -> new GeoMediasDto(entry.getKey(), entry.getValue())).collect(Collectors.toList());

		List<TagEntity> tagEntitys = tagService.findTagsByRelInfoAndTagType(id, RelType.trace, TagType.commons);
		List<TagOutputDto> tags = tagEntitys.stream().map(entity -> new TagOutputDto(entity)).toList();
		System.err.println("tagEntitys.size(): " + tagEntitys.size());

		List<TagEntity> cityTagEntitys = tagService.findTagsByRelInfoAndTagTypeForStayedCities(id, TagType.city);
		List<TagOutputDto> cityTags = cityTagEntitys.stream().map(entity -> new TagOutputDto(entity)).toList();

		model.addAttribute("tags", tags);
		model.addAttribute("cityTags", cityTags);
		model.addAttribute("trace", trace);
		model.addAttribute("geoMedias", geoMedias);
		model.addAttribute("mediaFiles", allMediaFiles);
		model.addAttribute("pathCoordinatesGroups", pathCoordinates);
		return "/trace/traceDetail";
	}

	@GetMapping("/trace/traceList")
	public String showTraceList(@RequestParam(defaultValue = "-1") Integer tagId, Model model) {
		model.addAttribute("tagId", tagId);
		System.err.println("tagId" + tagId);
	    return "/trace/traceList";
	}
	
	

	@PostMapping("/trace/traceList")
	@ResponseBody
	public ResultData<List<TraceListOutputDto>> getTraceList(@RequestBody TraceListRequestDto traceListRequestDto) {

		System.err.println(traceListRequestDto);
		List<TraceEntity> traceEntitys = traceService.findTracesForPrintList(traceListRequestDto);
		List<TraceListOutputDto> traces = traceEntitys.stream().map(entity -> new TraceListOutputDto(entity, path))
				.toList();
		
		
		
		for(TraceListOutputDto trace : traces) {
			List<TagEntity> tagEntitys = tagService.findTagsByRelInfoAndTagType(trace.getId(), RelType.trace, TagType.commons);
			List<TagOutputDto> tags = tagEntitys.stream().map(entity -> new TagOutputDto(entity)).toList();
			
			List<TagEntity> cityTagEntitys = tagService.findTagsByRelInfoAndTagTypeForStayedCities(trace.getId(), TagType.city);
			List<TagOutputDto> cityTags = cityTagEntitys.stream().map(entity -> new TagOutputDto(entity)).toList();
			trace.setTags(tags);
			trace.setCityTags(cityTags);
		}
		
		
		
		System.err.println(traces);

		return ResultData.ofData("S-1", "success", "traces", traces);
	}

	@GetMapping("/trace/increaseHitCount")
	@ResponseBody
	public ResultData<Integer> increaseHitCount(Long id) {
		// id null인경우 예외처리 할 것

		traceService.increaseHitCount(id);
		Integer hitCount = traceService.getHitCount(id);
		return ResultData.ofData("S-1", "success", "hitCount", hitCount);
	}

	
	
	@GetMapping("/trace/deleteTrace")
	public String deleteTrace(Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		// id null인경우 예외처리 할 것
		LoginInfoDTO loginInfo = (LoginInfoDTO) request.getAttribute("loginInfo");
		UserDto user = loginInfo.getUserDto();
		TraceEntity trace = traceService.findTraceById(id);
		if(id==null || trace == null) {	
			MessageResponse message = new MessageResponse("ERROR","존재하지 않는 발자국 입니다.", "");
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/trace/traceList";
		}
		
		if(trace.getWriterId() != loginInfo.getUserId()) {
			
			MessageResponse message = new MessageResponse("ERROR","삭제권한이 없는 회원님 입니다.", "");
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/trace/traceDetail?id="+id;
		}
		
		traceService.updateDeleteFlug(id);
		
		MessageResponse message = new MessageResponse("INFO",String.format("%d번 게시그이 삭제되었습니다.", id), "발자국 삭제가 완료되었습니다.");
		redirectAttributes.addFlashAttribute("message", message);	
		return "redirect:/trace/traceList";
	}
	
	
	
	@GetMapping("/trace/showModifyTraceDetail")
	public String showModifyTraceDetail(Long id, Model model) {
		// 1. trace 가지고 오기
		TraceEntity traceEntity = traceService.findTraceById(id);

		// todo 예외처리 해주기. 작성 권한등 확인.

		// 2. 모든 이미지 들고오기.
		List<MediaFileEntity> mediaFileEntitys = traceService.findAllMedaFileByTraceIdFromGeoMedia(id);

		// 3. dto만들어서 뿌려주기
		List<MediaFileDto> mediaFileDtos = mediaFileEntitys.stream().map(entity -> new MediaFileDto(entity, path))
				.toList();
		
		List<TagEntity> tagEntitys = tagService.findTagsByRelInfoAndTagType(id, RelType.trace, TagType.commons);
		List<TagOutputDto> tags = tagEntitys.stream().map(entity -> new TagOutputDto(entity)).toList();

		model.addAttribute("mediaFiles", mediaFileDtos);
		model.addAttribute("tags", tags);
		model.addAttribute("trace", traceEntity);

		return "/trace/modifyTraceDetail";
	}
//	@PostMapping("/trace/writeTraceDetail")
//	public String doWriteTraceDetail(Long id, Model model) {
//		//1. trace 가지고 오기
////		TraceEntity traceEntity = traceService.findTraceById(id); 
////		
////		//todo 예외처리 해주기. 작성 권한등 확인.
////		
////		//2. 모든 이미지 들고오기.
////		List<MediaFileEntity> mediaFileEntitys =  traceService.findAllMedaFileByTraceIdFromGeoMedia(id); 
////		
////	    //3. dto만들어서 뿌려주기
////		List<MediaFileDto> mediaFileDtos = mediaFileEntitys.stream().map(entity-> new MediaFileDto(entity, path)).toList();
////		
////		model.addAttribute("mediaFiles", mediaFileDtos);
////		model.addAttribute("traceId", id);
////				
////		return "/trace/writeTraceDetail";
//	}
	
	@PostMapping("/trace/doModifyTraceDetail")
	@ResponseBody
	public ResultData<String> doModifyTraceDetail(@RequestBody WriteOrModifyTraceDetailDto writeOrModifyTraceDto) {
		System.err.println(writeOrModifyTraceDto.toString());

		Long traceId = writeOrModifyTraceDto.getId();
		TraceEntity traceEntity = traceService.findTraceById(traceId);
		// 예외처리 todo

		// trace 테이블 갱신
		String title = writeOrModifyTraceDto.getTitle().trim();
		traceEntity.setTitle(title);

		Long featuredImageId = writeOrModifyTraceDto.getFeaturedImageId();
		traceEntity.setFeaturedImageId(featuredImageId);
		traceEntity.setStatus(TraceStatus.done);

		traceService.updateTrace(traceEntity);

		// 삭제처리된 이미지들 갱신
		List<Long> deletedMediaFileIds = writeOrModifyTraceDto.getDeletedMediaFileIds();
		if (deletedMediaFileIds != null && deletedMediaFileIds.size() > 0) {
			traceService.markMediaFilesAsDeleted(deletedMediaFileIds);
			traceService.updateGeoMediaDeletionStatusForAllDeletedMediaFiles(writeOrModifyTraceDto.getId());
		}

		List<TagOutputDto> tags = writeOrModifyTraceDto.getTags();
		System.err.println("tags.size() : " + tags.size());
		List<TagMappingEntity> tagMappings = tags.stream()
				.map(tag -> new TagMappingEntity(traceId, RelType.trace, tag.getId())).toList();
		
		traceService.deleteTagMappings(traceId, RelType.trace);
		if(tagMappings!=null && tagMappings.size()>0) {
		    traceService.insertTagMappings(tagMappings);
		}

		String redirectUrl = "/trace/traceDetail?id=" + writeOrModifyTraceDto.getId(); // 원하는 리다이렉트 경로
		System.err.println(redirectUrl);
		return ResultData.ofData("S-1", "Success", "redirectUrl", redirectUrl);
	}
	

}
