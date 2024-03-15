package com.smw.project.balmam.dto;

import com.smw.project.balmam.entity.TagEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TagOutputDto {
	private Long id;
    private String name;
    private String color;
	public TagOutputDto(TagEntity entity) {
		super();
		this.id = entity.getId();
		this.name = entity.getName();
		this.color = entity.getColor();
	}

    

}
