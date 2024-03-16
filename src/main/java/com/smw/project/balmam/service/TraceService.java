package com.smw.project.balmam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smw.project.balmam.entity.GeoMediaEntity;
import com.smw.project.balmam.entity.GeoMediaFileEntity;
import com.smw.project.balmam.entity.MediaFileEntity;
import com.smw.project.balmam.entity.PathCoordinateEntity;
import com.smw.project.balmam.entity.PathCoordinatesGroupEntity;
import com.smw.project.balmam.entity.StayedCityEntity;
import com.smw.project.balmam.entity.TagMappingEntity;
import com.smw.project.balmam.entity.TraceEntity;
import com.smw.project.balmam.repository.GeoMediaFileRepository;
import com.smw.project.balmam.repository.GeoMediaRepository;
import com.smw.project.balmam.repository.PathCoordinateEntityRepository;
import com.smw.project.balmam.repository.PathCoordinatesGroupRepository;
import com.smw.project.balmam.repository.StayedCityRepository;
import com.smw.project.balmam.repository.TagMappingRepository;
import com.smw.project.balmam.repository.TraceRepository;

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
	

	public void insertTrace(TraceEntity traceEntity) {
		traceRepository.insertTrace(traceEntity);
		
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
	
	
	

	

}
