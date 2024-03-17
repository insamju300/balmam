package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.smw.project.balmam.entity.BookmarkEntity;
import com.smw.project.balmam.entity.LikeEntity;

@Mapper
public interface BookmarkRepository {


    @Insert("INSERT INTO BOOKMARK (relType, relId, memberId) VALUES (#{bookmarkEntity.relType}, #{bookmarkEntity.relId}, #{bookmarkEntity.memberId})")
	void bookmarkIncrease(@Param("bookmarkEntity") BookmarkEntity bookmarkEntity);

    @Select("SELECT COUNT(*) FROM `BOOKMARK` WHERE relId = #{bookmarkEntity.relId} AND relType = #{bookmarkEntity.relType}")
    Integer countByRelIdAndRelType(@Param("bookmarkEntity") BookmarkEntity bookmarkEntity);

    
    @Delete("DELETE FROM `BOOKMARK` WHERE relType = #{bookmarkEntity.relType} AND relId = #{bookmarkEntity.relId} AND memberId = #{bookmarkEntity.memberId}")
    void bookmarkDecrease(@Param("bookmarkEntity") BookmarkEntity bookmarkEntity);



}
