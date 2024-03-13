package com.smw.project.balmam.dto;

import java.util.List;

import lombok.Data;

@Data
public class GeoMediasDto {
	private CoordinateDto coordinate;
	private List<MediaFileDto> mediaFiles;
}
