package com.smw.project.balmam.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import lombok.Data;
@Data
public class RouteRecordingDTO {
    private Timestamp recordingStartTime; //경로 녹화 시작 시간
    private Timestamp recordingEndTime;   //경로 녹화 종료 시간
    private Long totalPauseTime;          //총 일시정지 기간
    private List<List<PathCoordinateDto>> pathCoordinatesGroups; //경로 그룹
    private List<StayedCityDto> stayedCities;
    private List<GeoMediasDto> geoMedias;
    
}
