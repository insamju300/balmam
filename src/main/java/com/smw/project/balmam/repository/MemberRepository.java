package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.smw.project.balmam.entity.MemberEntity;

@Mapper
public interface MemberRepository {

	@Insert("INSERT INTO Member (email, password, nickname, introduction, profileImageId) " +
            "VALUES (#{email}, SHA2(#{password}, 256), #{nickname}, #{introduction}, #{profileImageId})")
	@Options(useGeneratedKeys=true, keyProperty="id")
	void insertMember(MemberEntity memberEntity);
	
	@Select("SELECT COUNT(*) > 0 FROM Member WHERE nickname = #{nickname}")
	boolean isExistsNickname(String nickName);
	
	@Select("SELECT COUNT(*) > 0 FROM Member WHERE email = #{email}")
	boolean isExistsEmail(String email);

	@Select("SELECT * FROM Member WHERE email = #{email}")
	MemberEntity findMemberByEmail(String email);

    @Update("UPDATE Member Set isWithdrawn=TRUE, withdrawalDate=Now()")
	void updateWithdrawn(String email);
	
}
