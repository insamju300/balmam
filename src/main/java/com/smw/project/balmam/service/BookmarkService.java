package com.smw.project.balmam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smw.project.balmam.entity.BookmarkEntity;
import com.smw.project.balmam.entity.LikeEntity;
import com.smw.project.balmam.enums.RelType;
import com.smw.project.balmam.repository.BookmarkRepository;
import com.smw.project.balmam.repository.TraceRepository;

@Service
public class BookmarkService {
	@Autowired
	BookmarkRepository bookmarkRepository;
	@Autowired
	TraceRepository traceRepository;

//	//todo 트랜잭셔널
//	public Integer likeIncrease(LikeEntity likeEntity) {
//		// TODO Auto-generated method stub
//		bookmarkRepository.bookmarkIncrease(likeEntity);
//		
//		if(likeEntity.getRelType() == RelType.trace) {
//			traceRepository.increaseLikeCount(likeEntity.getRelId());
//		}
//		
//		return bookmarkRepository.countByRelIdAndRelType(likeEntity);
//	}
	public Integer BookmarkDecrease(BookmarkEntity bookmarkEntity) {
		// TODO Auto-generated method stub
		bookmarkRepository.bookmarkDecrease(bookmarkEntity);
		
		if(bookmarkEntity.getRelType() == RelType.trace) {
			traceRepository.descreaseBookmarkCount(bookmarkEntity.getRelId());
		}
		
		return bookmarkRepository.countByRelIdAndRelType(bookmarkEntity);
	}
	public Integer bookmarkIncrease(BookmarkEntity bookmarkEntity) {
		// TODO Auto-generated method stub
		bookmarkRepository.bookmarkIncrease(bookmarkEntity);
		
		if(bookmarkEntity.getRelType() == RelType.trace) {
			traceRepository.increaseBookmarkCount(bookmarkEntity.getRelId());
		}
		
		return bookmarkRepository.countByRelIdAndRelType(bookmarkEntity);
	}

}
