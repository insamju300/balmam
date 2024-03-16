package com.smw.project.balmam.dto;

import java.util.List;

import lombok.Data;

@Data
public class GeoMediasDto {
	public GeoMediasDto(CoordinateDto coordinate, List<MediaFileDto> mediaFiles) {
		this.coordinate = coordinate;
		this.mediaFiles = mediaFiles;
	}
	private CoordinateDto coordinate;
	private List<MediaFileDto> mediaFiles;
}
