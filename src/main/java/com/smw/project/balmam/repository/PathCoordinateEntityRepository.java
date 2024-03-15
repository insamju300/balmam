package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

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
}


