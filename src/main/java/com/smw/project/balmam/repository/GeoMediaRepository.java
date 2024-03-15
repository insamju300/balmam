package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

import com.smw.project.balmam.entity.GeoMediaEntity;

@Mapper
public interface GeoMediaRepository {

	@Insert("INSERT INTO GeoMedia (lat , lng, traceId) " +
            "VALUES (#{lat}, #{lng}, #{traceId})")
	@Options(useGeneratedKeys=true, keyProperty="id")
	void insertGeoMedia(GeoMediaEntity geoMedia);
	
	//다시 볼 것
    @Update("""
				UPDATE GeoMedia gm
				INNER JOIN 
				(
				    SELECT geomediaId
				    FROM GeoMediaFile
				    GROUP BY geomediaId
				    HAVING SUM(isDeleted = FALSE) = 0
				) gmf ON gm.id = gmf.geomediaId
				SET gm.isDeleted = TRUE, gm.deletedDate = NOW()
				WHERE gm.isDeleted = FALSE AND gm.traceId = #{traceId}
    		""")
    void updateGeoMediaDeletionStatusForAllDeletedMediaFiles(Long traceId);


}
