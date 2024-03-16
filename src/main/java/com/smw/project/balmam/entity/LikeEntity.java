package com.smw.project.balmam.entity;

import java.sql.Timestamp;

import com.smw.project.balmam.enums.RelType;
import com.smw.project.balmam.enums.TagType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LikeEntity {
	private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private RelType relType;
    private Long relId;
    private Long memberId;
    
}
