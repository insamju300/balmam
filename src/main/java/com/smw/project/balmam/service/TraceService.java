package com.smw.project.balmam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smw.project.balmam.dto.PathCoordinateDto;
import com.smw.project.balmam.dto.TraceListRequestDto;
import com.smw.project.balmam.entity.GeoMediaEntity;
import com.smw.project.balmam.entity.GeoMediaFileEntity;
import com.smw.project.balmam.entity.MediaFileEntity;
import com.smw.project.balmam.entity.PathCoordinateEntity;
import com.smw.project.balmam.entity.PathCoordinatesGroupEntity;
import com.smw.project.balmam.entity.StayedCityEntity;
import com.smw.project.balmam.entity.TagMappingEntity;
import com.smw.project.balmam.entity.TraceEntity;
import com.smw.project.balmam.enums.RelType;
import com.smw.project.balmam.enums.TagType;
import com.smw.project.balmam.repository.GeoMediaFileRepository;
import com.smw.project.balmam.repository.GeoMediaRepository;
import com.smw.project.balmam.repository.PathCoordinateEntityRepository;
import com.smw.project.balmam.repository.PathCoordinatesGroupRepository;
import com.smw.project.balmam.repository.StayedCityRepository;
import com.smw.project.balmam.repository.TagMappingRepository;
import com.smw.project.balmam.repository.TraceRepository;
import com.smw.project.balmam.utill.JsonFileWriter;

@Service
public class TraceService {
	@Autowired
	TraceRepository traceRepository;
	
	@Autowired
	PathCoordinatesGroupRepository pathCoordinatesGroupRepository;
	
	@Autowired
	PathCoordinateEntityRepository pathCoordinatesRepository;
	
	@Autowired
	StayedCityRepository stayedCityRepository;	
	
	@Autowired
	GeoMediaRepository geoMediaRepository;
	
	@Autowired
	GeoMediaFileRepository geoMediaFileRepository;
	
	@Autowired
	TagMappingRepository tagMappingRepository;
	
	@Value("${file.trace.jsonFiles}")
	private String jsonFilePath;
	

	public void insertTrace(TraceEntity traceEntity) {
		traceRepository.insertTrace(traceEntity);
		
	}
	
	public void writeToJsonFile(List<List<PathCoordinateDto>> pathCoordinates, Long traceId) {
		String fullPath = jsonFilePath + "/" + traceId + ".json";
		JsonFileWriter.writeToJsonFile(pathCoordinates, fullPath);
	}

	public void insertPathCoordinatesGroup(PathCoordinatesGroupEntity pathCoordinatesGroup) {
		// TODO Auto-generated method stub
		pathCoordinatesGroupRepository.insertPathCoordinatesGroupEntity(pathCoordinatesGroup);
		
	}

	public void insertPathCoordinates(List<PathCoordinateEntity> pathCoordinates) {
		// TODO Auto-generated method stub
		pathCoordinatesRepository.insertPathCoordinates(pathCoordinates);
	}

	public void insertStayedCities(List<StayedCityEntity> stayedCities) {
		stayedCityRepository.insertStayedCities(stayedCities);
		
	}



	public void insertGeoMedia(GeoMediaEntity geoMedia) {
		geoMediaRepository.insertGeoMedia(geoMedia);
	}

	public void insertGeoMediaFiles(List<GeoMediaFileEntity> geoMediaFiles) {
		geoMediaFileRepository.insertGeoMediaFiles(geoMediaFiles);
		
	}

	public TraceEntity findTraceById(Long id) {
		// TODO Auto-generated method stub
		return traceRepository.findTraceByid(id);
	}

	public List<MediaFileEntity> findAllMedaFileByTraceIdFromGeoMedia(Long id) {
		// TODO Auto-generated method stub
		return geoMediaFileRepository.findAllMedaFileByTraceIdFromGeoMedia(id);
	}

	public void updateTrace(TraceEntity traceEntity) {
		traceRepository.updateTrace(traceEntity);
		
	}

	public void markMediaFilesAsDeleted(List<Long> deletedMediaFileIds) {
		geoMediaFileRepository.markMediaFilesAsDeleted(deletedMediaFileIds);
	}

	public void updateGeoMediaDeletionStatusForAllDeletedMediaFiles(Long traceId) {
		// TODO Auto-generated method stub
		geoMediaRepository.updateGeoMediaDeletionStatusForAllDeletedMediaFiles(traceId);
	}

	public void insertTagMappings(List<TagMappingEntity> tagMappings) {
		tagMappingRepository.insertTagMappings(tagMappings);
		
	}

	public List<PathCoordinateEntity> findPathCoordinatesByTraceIdInPathCoordinatesGroup(Long id) {
		return pathCoordinatesRepository.findPathCoordinatesByTraceIdInPathCoordinatesGroup(id);
		
	}

	public List<MediaFileEntity> findGeoMedaFilesByTraceId(Long id) {
		// TODO Auto-generated method stub
		return geoMediaFileRepository.findGeoMedaFilesByTraceId(id);
	}
	
	public TraceEntity findTraceByIdAndUserIdForPrintDetial(Long traceId, Long userId){
		return traceRepository.findTraceByIdAndUserIdForPrintDetial(traceId, userId);
	}

	public List<TraceEntity> findTracesForPrintList(TraceListRequestDto traceListRequestDto) {
		// TODO Auto-generated method stub
		return traceRepository.findTracesForPrintList(traceListRequestDto);
	}

	public void increaseHitCount(Long id) {
		traceRepository.increaseHitCount(id);
		
	}

	public Integer getHitCount(Long id) {
		// TODO Auto-generated method stub
		return traceRepository.getHitCount(id);
	}

	public void updateDeleteFlug(Long id) {
		traceRepository.updateDeleteFlug(id);
		
	}

	public void deleteTagMappings(Long traceId, RelType reltype) {
		// TODO Auto-generated method stub
		tagMappingRepository.deleteMappings(traceId, reltype);
		
	}

	public int getTraceCountFromMemberId(Long writerId) {
		// TODO Auto-generated method stub
		return traceRepository.getTraceCountFromMemberId(writerId);
		
	}
	
	public int getCityCountFromTraceWriterId(Long id) {
		return stayedCityRepository.getCityCountFromTraceWriterId(id);	
	}
	




	
	
	

	

}
