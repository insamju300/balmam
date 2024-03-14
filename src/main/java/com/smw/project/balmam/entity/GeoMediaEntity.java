package com.smw.project.balmam.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.smw.project.balmam.dto.CoordinateDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeoMediaEntity {
    private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private BigDecimal lat;
    private BigDecimal lng;
    private Long traceId;
    private Boolean isDeleted;
    private Timestamp deletedDate;
	
    public GeoMediaEntity(CoordinateDto dto, Long traceId ) {
		super();
	    this.lat = BigDecimal.valueOf(dto.getLat());
	    this.lng = BigDecimal.valueOf(dto.getLng());
	    this.traceId = traceId;
	}
    
}
