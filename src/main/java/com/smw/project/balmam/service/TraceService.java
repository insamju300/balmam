package com.smw.project.balmam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smw.project.balmam.entity.CityTagEntity;
import com.smw.project.balmam.entity.GeoMediaEntity;
import com.smw.project.balmam.entity.GeoMediaFilesEntity;
import com.smw.project.balmam.entity.PathCoordinatesGroupEntity;
import com.smw.project.balmam.entity.StayedCityEntity;
import com.smw.project.balmam.entity.TraceEntity;
import com.smw.project.balmam.entity.pathCoordinateEntity;
import com.smw.project.balmam.repository.CityTagRepository;
import com.smw.project.balmam.repository.GeoMediaFileRepository;
import com.smw.project.balmam.repository.GeoMediaRepository;
import com.smw.project.balmam.repository.PathCoordinateEntityRepository;
import com.smw.project.balmam.repository.PathCoordinatesGroupRepository;
import com.smw.project.balmam.repository.StayedCityRepository;
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
	CityTagRepository cityTagRepository;	
	
	@Autowired
	GeoMediaRepository geoMediaRepository;
	
	@Autowired
	GeoMediaFileRepository geoMediaFileRepository;
	

	public void insertTrace(TraceEntity traceEntity) {
		traceRepository.insertTrace(traceEntity);
		
	}

	public void insertPathCoordinatesGroup(PathCoordinatesGroupEntity pathCoordinatesGroup) {
		// TODO Auto-generated method stub
		pathCoordinatesGroupRepository.insertPathCoordinatesGroupEntity(pathCoordinatesGroup);
		
	}

	public void insertPathCoordinates(List<pathCoordinateEntity> pathCoordinates) {
		// TODO Auto-generated method stub
		pathCoordinatesRepository.insertPathCoordinates(pathCoordinates);
	}

	public void insertStayedCities(List<StayedCityEntity> stayedCities) {
		stayedCityRepository.insertStayedCities(stayedCities);
		
	}

	public void insertCityTags(List<CityTagEntity> cityTags) {
		// TODO Auto-generated method stub
		cityTagRepository.insertCityTags(cityTags);
		
	}

	public void insertGeoMedia(GeoMediaEntity geoMedia) {
		geoMediaRepository.insertGeoMedia(geoMedia);
	}

	public void insertGeoMediaFiles(List<GeoMediaFilesEntity> geoMediaFiles) {
		geoMediaFileRepository.insertGeoMediaFiles(geoMediaFiles);
		
	}
	

}
