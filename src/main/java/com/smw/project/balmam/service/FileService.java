package com.smw.project.balmam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smw.project.balmam.entity.MediaFileEntity;
import com.smw.project.balmam.repository.FileRepository;

@Service
public class FileService {

	@Autowired
	private FileRepository fileRepository;
	
	
	public void insertMediaFile(MediaFileEntity mediaFileEntity) {
		fileRepository.insertMediaFile(mediaFileEntity);
	}


	public MediaFileEntity findFileById(Long id) {
		return fileRepository.findFileById(id);
		
	}


}
