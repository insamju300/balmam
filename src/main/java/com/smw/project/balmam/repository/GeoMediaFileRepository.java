package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.smw.project.balmam.entity.GeoMediaFileEntity;
import com.smw.project.balmam.entity.MediaFileEntity;

@Mapper
public interface GeoMediaFileRepository {

	
    @Insert({
        "<script>",
        "INSERT INTO GeoMediaFile (geoMediaId, mediaFileId) VALUES ",
        "<foreach collection='geoMediaFiles' item='geoMediaFile' separator=','>",
            "(#{geoMediaFile.geoMediaId}, #{geoMediaFile.mediaFileId})",
        "</foreach>",
        "</script>"
    })
    @Options(useGeneratedKeys=true, keyProperty="id")
    void insertGeoMediaFiles(@Param("geoMediaFiles") List<GeoMediaFileEntity> geoMediaFiles);

    
	List<MediaFileEntity> findAllMedaFileByTraceIdFromGeoMedia(Long traceId);

}
