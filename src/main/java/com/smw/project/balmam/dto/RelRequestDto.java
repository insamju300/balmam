package com.smw.project.balmam.dto;

import com.smw.project.balmam.enums.RelType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RelRequestDto {
	
	private RelType relType;
	private Long relId;

}
