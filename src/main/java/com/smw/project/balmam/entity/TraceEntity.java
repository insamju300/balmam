package com.smw.project.balmam.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.smw.project.balmam.dto.RouteRecordingDTO;
import com.smw.project.balmam.enums.MediaType;
import com.smw.project.balmam.enums.TraceStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TraceEntity {

	private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private Long writerId;
    private String title;
    private Timestamp recordingStartTime;
    private Timestamp recordingEndTime;
    private Long totalPauseTime;
    private Long featuredImageId;
    private Integer hitCount;
    private Integer likeCount;
    private Integer bookmarkCount;
    private Integer commentCount;
    private Long orderPoint;
    private TraceStatus status;
    private Boolean isDeleted;
    private Timestamp deletedDate;
    
    
    private String extra__writerNickname;
    private Long extra__writerProfileImageId;
    private String extra__writerProfileImageName;    
    private String extra__featuredImageName;
    private MediaType extra__featuredImageType;
    private String extra__featuredImageThumbnailName;
    

    private Boolean extra__isLiked;
    private Boolean extra__isBookmarked;
    private Boolean extra__isAccessible;
    
    
    public TraceEntity(RouteRecordingDTO routeRecordingDTO, Long writerId) {
		this.recordingStartTime = routeRecordingDTO.getRecordingStartTime();
		this.recordingEndTime = routeRecordingDTO.getRecordingEndTime();
		this.totalPauseTime = routeRecordingDTO.getTotalPauseTime();
		this.status=TraceStatus.recordingDone;
		this.writerId = writerId;
	}
    
    
}
