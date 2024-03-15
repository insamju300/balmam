package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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


    @Select("""
    		SELECT mediaFiles.* FROM geoMedia
			INNER JOIN geoMediaFile 
			ON geoMedia.id = geoMediaFile.geoMediaId AND geoMedia.isDeleted = FALSE AND geoMediaFile.isDeleted = FALSE
			INNER JOIN mediaFiles ON geoMediaFile.mediaFileId = mediaFiles.id
			WHERE geomedia.traceId = #{traceId};
    		""")
	List<MediaFileEntity> findAllMedaFileByTraceIdFromGeoMedia(Long traceId);

    
    @Update({
        "<script>",
        "UPDATE GeoMediaFile",
        "SET isDeleted = TRUE,",
        "deletedDate = NOW()",
        "WHERE mediaFileId IN",
        "<foreach item='mediaFileId' collection='mediaFileIds' open='(' separator=',' close=')'>",
        "#{mediaFileId}",
        "</foreach>",
        "</script>"
    })
    void markMediaFilesAsDeleted(@Param("mediaFileIds") List<Long> mediaFileIds);
}
