package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.smw.project.balmam.Enum.EmailAuthenticationType;
import com.smw.project.balmam.entity.EmailAuthenticationsEntity;

@Mapper
public interface EmailAuthentication {

	@Insert("INSERT INTO emailAuthentications (memberId, token, expiresAt, type) VALUES (#{memberId}, #{token}, #{expiresAt}, #{type})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(EmailAuthenticationsEntity emailAuthEntity);

	@Select("SELECT * FROM emailAuthentications WHERE TOKEN = #{token} AND `TYPE` = #{type}")
	EmailAuthenticationsEntity findEmailAuthenticationsFromToken(String token, EmailAuthenticationType type);

	@Update("""
			<script>
			UPDATE EmailAuthentications AS ea
			<if test="type.toString() == 'emailVerification'">
			  INNER JOIN Member AS m ON ea.memberId = m.id
			</if>
			SET ea.verified = TRUE
			<if test="type.toString() == 'emailVerification'">
			  , m.emailVerified = TRUE
			</if>
			WHERE ea.token = #{token}
			</script>
			""")
	void updateVerifiedValue(String token, EmailAuthenticationType type);
}
