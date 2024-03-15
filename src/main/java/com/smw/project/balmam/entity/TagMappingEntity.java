package com.smw.project.balmam.entity;

import java.sql.Timestamp;

import com.smw.project.balmam.enums.RelType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TagMappingEntity {
    private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private Long relId;
    private RelType relType;
    private Long tagId;
    
	public TagMappingEntity(Long relId, RelType relType, Long tagId) {
		super();
		this.relId = relId;
		this.relType = relType;
		this.tagId = tagId;
	}
    
    

}
