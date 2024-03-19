package com.smw.project.balmam.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.smw.project.balmam.entity.ReplyEntity;

@Mapper
public interface ReplyRepository {
	@Select("""
			
			WITH RECURSIVE CommentHierarchy AS (
			  -- Anchor member: Select the top-level comment using the passed parameter
			  SELECT id, parentId, 1 AS depth
			  FROM ReplyEntity
			  WHERE id = #{topCommentId} -- Replace @topCommentId with the specific parameter syntax for your SQL environment
			  
			  UNION ALL
			  
			  -- Recursive member: Select replies, incrementing depth at each level
			  SELECT r.id, r.parentId, ch.depth + 1
			  FROM ReplyEntity r
			  JOIN CommentHierarchy ch ON r.parentId = ch.id
			)
			-- Select the count of all replies (excluding the top-level comment itself)
			SELECT COUNT(*) - 1 AS total_replies
			FROM CommentHierarchy;
			""")
	public Integer getReplyCountByTopCommentID(Long topCommentId);
	
	
	/*
	 * @Select(""" SELECT r.*, (SELECT COUNT(*) FROM ReplyEntity AS sub WHERE
	 * sub.parentId = r.id) AS extra__replyCount FROM ReplyEntity AS r WHERE
	 * r.parentId IS NULL AND r.id > #{minReplyId} ORDER BY r.id ASC LIMIT
	 * #{limitNumber} """) public List<ReplyEntity> find
	 */
	
}
