package com.smw.project.balmam.dto;

import java.time.LocalDateTime;

import com.smw.project.balmam.entity.MediaFileEntity;

import lombok.Getter;

@Getter
public class MediaFileDto {

	private Long id;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private String url;
    private Long size;
    private String type; // e.g., "video" or "image"
    //private List<MediaFileDto> subFiles; // Nested DTOs for subFiles
    public MediaFileDto(MediaFileEntity mediaFileEntity) {
		this.id = mediaFileEntity.getId();
		this.regDate = mediaFileEntity.getRegDate();
		this.updateDate = mediaFileEntity.getUpdateDate();
		this.url = mediaFileEntity.getPath() + "/" + mediaFileEntity.getName();
		this.size = mediaFileEntity.getSize();
		this.type = mediaFileEntity.getType();
	}
    // Getters and Setters
    // Constructors
}