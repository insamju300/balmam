package com.smw.project.balmam.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class WriteOrModifyTraceDetailDto {
	Long id;
	String title;
	Long featuredImageId;
	List<Long> deletedMediaFileIds;
	List<TagOutputDto> tags;
}
