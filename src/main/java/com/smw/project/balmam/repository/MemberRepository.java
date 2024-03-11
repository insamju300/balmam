package com.smw.project.balmam.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.smw.project.balmam.dto.MemberInputDto;
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

	@Select("SELECT m.*, f.name as extra__profileImageName FROM Member m left outer join mediaFiles f on m.profileImageId=f.id WHERE m.emial = #{email}")
	MemberEntity findMemberByEmail(String email);
	
	@Select("SELECT m.*, f.name as extra__profileImageName FROM Member m left outer join mediaFiles f on m.profileImageId=f.id WHERE m.id = #{id}")
	MemberEntity findMemberById(Long id);

    @Update("UPDATE Member Set isWithdrawn=TRUE, withdrawalDate=Now()")
	void updateWithdrawn(String email);

    @Update("""
    		<script>
    		
    		UPDATE Member Set 
    		<if test="email != null">
    		    email = #{email},
    		    password = #{password},
    		    nickname = #{nickname},
    		    introduction = #{introduction},
    		    profileImageId = #{profileImageId},
    		</if>
    		where id=#{id}
    		</script>
    		""")
	void updateMember(MemberInputDto memberInputDto);
	
}
