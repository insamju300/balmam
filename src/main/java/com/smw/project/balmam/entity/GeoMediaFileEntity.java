package com.smw.project.balmam.entity;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeoMediaFileEntity {

	private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private Long geoMediaId;
    private Long mediaFileId;
    private Boolean isDeleted;
    private Timestamp deletedDate;
    
    public GeoMediaFileEntity(Long geoMediaId, Long mediaFileId) {
		this.geoMediaId = geoMediaId;
		this.mediaFileId = mediaFileId;
	}
}
