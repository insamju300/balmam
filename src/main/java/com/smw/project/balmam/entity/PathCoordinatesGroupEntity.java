package com.smw.project.balmam.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class PathCoordinatesGroupEntity {

	private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private Long traceId;
    private Integer order;
    
    public PathCoordinatesGroupEntity(Long traceId, Integer order) {
    	this.traceId = traceId;
    	this.order = order;
	}
}
