package com.smw.project.balmam.entity;

import java.sql.Timestamp;

import com.smw.project.balmam.enums.RelType;

public class ReplyEntity {
	private Long id;
	private Timestamp regDate;
	private Timestamp updateDate;
	private Long parentId;
	private RelType relType;
	private Long relId;
	private Integer depth;
	private String body;
	private Long writerId;
    private Boolean isDeleted;
    private Timestamp deletedDate;
}
