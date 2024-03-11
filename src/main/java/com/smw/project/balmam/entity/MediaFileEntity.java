package com.smw.project.balmam.entity;

import java.time.LocalDateTime;

import lombok.Getter;
@Getter
public class MediaFileEntity {

    private Long id;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private String name;
    private Long size;
    private String type;
    
	public MediaFileEntity(String name, Long size, String type) {
		super();
		this.name = name;
		this.size = size;
		this.type = type;
	}
    
    
    
    
    
    //private String type; // e.g., "video" or "image"
    //private List<MediaFileDto> subFiles; // Nested DTOs for subFiles

    // Getters and Setters
    // Constructors
}