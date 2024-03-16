package com.smw.project.balmam.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.smw.project.balmam.dto.PathCoordinateDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PathCoordinateEntity {
    private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private Long pathCoordinatesGroupId;
    private Timestamp time;
    private BigDecimal lat;
    private BigDecimal lng;
    
    public PathCoordinateEntity(PathCoordinateDto dto, Long pathCoordinatesGroupId) {
        this.lat = dto.getLat();
        this.lng = dto.getLng();
        this.time = dto.getTime();
        this.pathCoordinatesGroupId = pathCoordinatesGroupId;
    }
}
