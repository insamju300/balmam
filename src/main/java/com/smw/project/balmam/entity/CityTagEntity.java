package com.smw.project.balmam.entity;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CityTagEntity {
	private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private String name;
    private String color;
    
	public CityTagEntity(String name, String color) {
		super();
		this.name = name;
		this.color = color;
	}
    
    
}
