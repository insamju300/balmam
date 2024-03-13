package com.smw.project.balmam.entity;

import java.time.LocalDateTime;

import com.smw.project.balmam.enums.MediaType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@NoArgsConstructor
public class MediaFileEntity {

    private Long id;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private String name;
    private String thumbnailName;
    private Long size;
    private MediaType type;
    
	public MediaFileEntity(String name, Long size, MediaType type) {
		super();
		this.name = name;
		this.size = size;
		this.type = type;
	}
	
	public MediaFileEntity(String name, String thumbnailName, Long size, MediaType type) {
		super();
		this.thumbnailName = thumbnailName;
		this.name = name;
		this.size = size;
		this.type = type;
	}
    
    
    
    
    
    //private String type; // e.g., "video" or "image"
    //private List<MediaFileDto> subFiles; // Nested DTOs for subFiles

    // Getters and Setters
    // Constructors
}