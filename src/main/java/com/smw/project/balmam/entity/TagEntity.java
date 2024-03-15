package com.smw.project.balmam.entity;

import java.sql.Timestamp;

import com.smw.project.balmam.enums.TagType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TagEntity {
	private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private String name;
    private String color;
    private TagType type;
    
	public TagEntity(String name, String color, TagType type) {
		super();
		this.name = name;
		this.color = color;
		this.type = type;
	}

	public TagEntity(String name, TagType type) {
	   this.name = name;
	   this.type = type;
	}
    
    
}
