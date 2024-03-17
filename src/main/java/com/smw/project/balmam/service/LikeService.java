package com.smw.project.balmam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smw.project.balmam.entity.LikeEntity;
import com.smw.project.balmam.enums.RelType;
import com.smw.project.balmam.repository.LikeRepository;
import com.smw.project.balmam.repository.TraceRepository;

@Service
public class LikeService {
	@Autowired
	LikeRepository likeRepository;
	@Autowired
	TraceRepository traceRepository;

	//todo 트랜잭셔널
	public Integer likeIncrease(LikeEntity likeEntity) {
		// TODO Auto-generated method stub
		likeRepository.likeIncrease(likeEntity);
		
		if(likeEntity.getRelType() == RelType.trace) {
			traceRepository.increaseLikeCount(likeEntity.getRelId());
		}
		
		return likeRepository.countByRelIdAndRelType(likeEntity);
	}
	public Integer likeDecrease(LikeEntity likeEntity) {
		// TODO Auto-generated method stub
		likeRepository.likeDecrease(likeEntity);
		
		if(likeEntity.getRelType() == RelType.trace) {
			traceRepository.descreaseLikeCount(likeEntity.getRelId());
		}
		
		return likeRepository.countByRelIdAndRelType(likeEntity);
	}

}
