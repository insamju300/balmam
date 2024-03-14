package com.smw.project.balmam.entity;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeoMediaFilesEntity {

	private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private Long geoMediaId;
    private Long mediaFileId;
    private Boolean isDeleted;
    private Timestamp deletedDate;
    
    public GeoMediaFilesEntity(Long geoMediaId, Long mediaFileId) {
		this.geoMediaId = geoMediaId;
		this.mediaFileId = mediaFileId;
	}
}
