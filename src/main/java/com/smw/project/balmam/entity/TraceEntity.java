package com.smw.project.balmam.entity;

import java.sql.Timestamp;

import com.smw.project.balmam.dto.RouteRecordingDTO;
import com.smw.project.balmam.enums.TraceStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TraceEntity {

	private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private Long writerId;
    private Boolean isDeleted;
    private Timestamp deletedDate;
    private String title;
    private Timestamp recordingStartTime;
    private Timestamp recordingEndTime;
    private Long totalPauseTime;
    private Integer hitCount;
    private Integer likeCount;
    private Integer bookmarkCount;
    private Integer commentCount;
    private Integer orderPoint;
    private TraceStatus status;
    private String extra__writerNickname;
    
    public TraceEntity(RouteRecordingDTO routeRecordingDTO, Long writerId) {
		this.recordingStartTime = routeRecordingDTO.getRecordingStartTime();
		this.recordingEndTime = routeRecordingDTO.getRecordingEndTime();
		this.totalPauseTime = routeRecordingDTO.getTotalPauseTime();
		this.status=TraceStatus.recordingDone;
		this.writerId = writerId;
	}
    
    
}
