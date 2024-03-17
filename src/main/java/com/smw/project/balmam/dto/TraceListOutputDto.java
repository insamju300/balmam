package com.smw.project.balmam.dto;

import com.smw.project.balmam.entity.TraceEntity;
import com.smw.project.balmam.enums.MediaType;
import com.smw.project.balmam.utill.Ut;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TraceListOutputDto {
	private Long id;
	private String regDate;
	private String title;

    private Integer hitCount;
    private Integer likeCount;
    private Integer bookmarkCount;
    private Integer commentCount;
    
    private Long writerId;
    private String writerNickname;
    private String writerProfileImageUrl;
    
    private String featuredImageUrl;
    private Long orderPoint;
    
  


	public TraceListOutputDto(TraceEntity traceEntity, String path) {
		// TODO Auto-generated constructor stub
		this.id = traceEntity.getId();
		this.regDate = Ut.getTimeAgo(traceEntity.getRegDate());
		this.title = traceEntity.getTitle();
		
		this.hitCount = traceEntity.getHitCount();
		this.likeCount = traceEntity.getLikeCount();
		this.bookmarkCount = traceEntity.getBookmarkCount();
		this.commentCount = traceEntity.getCommentCount();
		this.writerId = traceEntity.getWriterId();
		this.writerNickname = traceEntity.getExtra__writerNickname();
		this.writerProfileImageUrl = path + "/" + traceEntity.getExtra__writerProfileImageName();
		if(traceEntity.getExtra__featuredImageType() == MediaType.image) {
			this.featuredImageUrl = path + "/" + traceEntity.getExtra__featuredImageName();    
		}else if(traceEntity.getExtra__featuredImageType() == MediaType.video) {
			this.featuredImageUrl = path + "/" + traceEntity.getExtra__featuredImageThumbnailName();
		}
		
		
		this.orderPoint = traceEntity.getOrderPoint();
		
		
	}


}
