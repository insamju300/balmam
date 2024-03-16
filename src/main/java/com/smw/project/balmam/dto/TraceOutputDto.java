package com.smw.project.balmam.dto;

import java.time.Duration;

import com.smw.project.balmam.entity.TraceEntity;
import com.smw.project.balmam.utill.Ut;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TraceOutputDto {
	private Long id;
	private String regDate;
	private String updateDate;
	private String title;
	private String recordingStartTimeAndEndTime;
	private String totalRecordingTime;
	private String actualRecordingTime;
    private Integer hitCount;
    private Integer likeCount;
    private Integer bookmarkCount;
    private Integer commentCount;
    
    private Long writerId;
    private String writerNickname;
    private String writerProfileImageUrl;
    
    private Boolean isLiked;
    private Boolean isBookmarked;
    private Boolean isAccessible;

	


	public TraceOutputDto(TraceEntity traceEntity, String path) {
		// TODO Auto-generated constructor stub
		this.id = traceEntity.getId();
		this.regDate = Ut.getTimeAgo(traceEntity.getRegDate());
		this.updateDate = Ut.getTimeAgo(traceEntity.getUpdateDate());
		if(regDate != updateDate) {
			updateDate += " 수정됨";
		}
		this.title = traceEntity.getTitle();
		this.recordingStartTimeAndEndTime =  Ut.convertTimestampToDateTimeFormattedString(traceEntity.getRecordingStartTime());
		this.recordingStartTimeAndEndTime +=" ~ " + Ut.convertTimestampToDateTimeFormattedString(traceEntity.getRecordingEndTime());

		Duration totalRecordingTimeDuration =Duration.between(traceEntity.getRecordingStartTime().toInstant(), traceEntity.getRecordingEndTime().toInstant());
		Duration puseTimeDuration = Duration.ofSeconds(traceEntity.getTotalPauseTime());
		this.totalRecordingTime = Ut.formatDuration(totalRecordingTimeDuration);
		this.actualRecordingTime = Ut.formatDuration(totalRecordingTimeDuration.minus(puseTimeDuration));
		this.hitCount = traceEntity.getHitCount();
		this.likeCount = traceEntity.getLikeCount();
		this.bookmarkCount = traceEntity.getBookmarkCount();
		this.commentCount = traceEntity.getCommentCount();
	    
		this.writerId = traceEntity.getWriterId();
		this.writerNickname = traceEntity.getExtra__writerNickname();
	    this.writerProfileImageUrl = path + "/" + traceEntity.getExtra__writerProfileImageName();
				//Ut.formatDuration();
		
		
	}

}
