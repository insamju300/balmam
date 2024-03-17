package com.smw.project.balmam.entity;

import java.sql.Timestamp;

import com.smw.project.balmam.dto.RelRequestDto;
import com.smw.project.balmam.enums.RelType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BookmarkEntity {
	public BookmarkEntity(RelRequestDto dto, Long memberId) {
    	this.relType = dto.getRelType();
    	this.relId = dto.getRelId();
    	this.memberId = memberId;
	}
	private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private RelType relType;
    private Long relId;
    private Long memberId;
}
