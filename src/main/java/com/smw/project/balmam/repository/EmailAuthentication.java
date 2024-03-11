package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.smw.project.balmam.entity.EmailAuthenticationsEntity;

@Mapper
public interface EmailAuthentication {

	@Insert("INSERT INTO emailAuthentications (member_id, token, expires_at) VALUES (#{memberId}, #{token}, #{expiresAt})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(EmailAuthenticationsEntity emailAuthEntity);

	@Select("SELECT * FROM emailAuthentications WHERE TOKEN = ${token}")
	EmailAuthenticationsEntity findEmailAuthenticationsFromToken(String token);

	@Update("""
			UPDATE EmailAuthentications AS ea
			INNER JOIN Member AS m ON ea.memberId = m.id
			SET ea.verified = TRUE,
			    m.emailVerified = TRUE
			WHERE ea.token = #{token}
			""")
	void updateVerifiedValue(String token);
}
