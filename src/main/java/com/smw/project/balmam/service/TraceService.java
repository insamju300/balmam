package com.smw.project.balmam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smw.project.balmam.entity.PathCoordinatesGroupEntity;
import com.smw.project.balmam.entity.TraceEntity;
import com.smw.project.balmam.repository.PathCoordinatesGroupRepository;
import com.smw.project.balmam.repository.TraceRepository;

@Service
public class TraceService {
	@Autowired
	TraceRepository traceRepository;
	
	@Autowired
	PathCoordinatesGroupRepository pathCoordinatesGroupRepository;

	public void insertTrace(TraceEntity traceEntity) {
		traceRepository.insertTrace(traceEntity);
		
	}

	public void insertPathCoordinatesGroup(PathCoordinatesGroupEntity pathCoordinatesGroup) {
		// TODO Auto-generated method stub
		pathCoordinatesGroupRepository.insertPathCoordinatesGroupEntity(pathCoordinatesGroup);
		
	}
	

}
