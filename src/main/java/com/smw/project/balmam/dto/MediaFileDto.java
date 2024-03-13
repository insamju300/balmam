package com.smw.project.balmam.dto;

import java.time.LocalDateTime;

import com.smw.project.balmam.entity.MediaFileEntity;
import com.smw.project.balmam.enums.MediaType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MediaFileDto {

	private Long id;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private String url;
    private Long size;
    private MediaType type; // e.g., "video" or "image"
    private String thumbnailUrl;
    
    //private List<MediaFileDto> subFiles; // Nested DTOs for subFiles
    public MediaFileDto(MediaFileEntity mediaFileEntity, String path) {
		this.id = mediaFileEntity.getId();
		this.regDate = mediaFileEntity.getRegDate();
		this.updateDate = mediaFileEntity.getUpdateDate();
		this.url = path + "/" + mediaFileEntity.getName();
		this.size = mediaFileEntity.getSize();
		this.type = mediaFileEntity.getType();
		if(mediaFileEntity.getThumbnailName()!=null) {
			this.thumbnailUrl = path + "/" + mediaFileEntity.getThumbnailName();
		}
		
//		if(type.equals(MediaType.video)){
//			thumbnailUrl = path + "/" + mediaFileEntity.getName();
//		}
		
	}
    // Getters and Setters
    // Constructors
}