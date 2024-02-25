package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.smw.project.balmam.entity.Test;

@Mapper
public interface TestRepository {
    @Select("SELECT NOW() AS currentTime")
    Test getCurrentTime();
}