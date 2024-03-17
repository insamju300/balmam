package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.smw.project.balmam.entity.LikeEntity;

@Mapper
public interface LikeRepository {


    @Insert("INSERT INTO `like` (relType, relId, memberId, regDate, updateDate) VALUES (#{likeEntity.relType}, #{likeEntity.relId}, #{likeEntity.memberId}, NOW(), NOW())")
    void likeIncrease(@Param("likeEntity") LikeEntity likeEntity);

    @Select("SELECT COUNT(*) FROM `like` WHERE relId = #{likeEntity.relId} AND relType = #{likeEntity.relType}")
    Integer countByRelIdAndRelType(@Param("likeEntity") LikeEntity likeEntity);

    
    @Delete("DELETE FROM `like` WHERE relType = #{likeEntity.relType} AND relId = #{likeEntity.relId} AND memberId = #{likeEntity.memberId}")
    void likeDecrease(@Param("likeEntity") LikeEntity likeEntity);

}
