package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.smw.project.balmam.entity.PathCoordinateEntity;

@Mapper
public interface PathCoordinateEntityRepository {
    @Insert({
        "<script>",
        "INSERT INTO PathCoordinate (lat, lng, time, pathCoordinatesGroupId) VALUES ",
        "<foreach collection='pathCoordinates' item='pathCoordinate' separator=','>",
            "(#{pathCoordinate.lat}, #{pathCoordinate.lng}, #{pathCoordinate.time}, #{pathCoordinate.pathCoordinatesGroupId})",
        "</foreach>",
        "</script>"
    })
    @Options(useGeneratedKeys=true, keyProperty="id")
    void insertPathCoordinates(@Param("pathCoordinates") List<PathCoordinateEntity> pathCoordinates);
    
    @Select("""
    SELECT pc.*
    FROM pathCoordinatesGroup pcg
    JOIN pathCoordinate pc ON pcg.id = pc.pathCoordinatesGroupId
    WHERE pcg.traceId = #{traceId}
    ORDER BY pcg.`order` ASC, pc.`time` ASC;
    """)
    List<PathCoordinateEntity> findPathCoordinatesByTraceIdInPathCoordinatesGroup(Long traceId);
    
    	
}


