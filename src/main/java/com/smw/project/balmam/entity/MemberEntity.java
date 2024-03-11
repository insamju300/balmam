package com.smw.project.balmam.entity;

import java.time.LocalDateTime;

import com.smw.project.balmam.dto.MemberJoinDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberEntity {

	private Long id;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private String email;
    private String password;
    private String nickname;
    private String introduction;
    private Long profileImageId;
    private boolean isWithdrawn;
    private LocalDateTime withdrawalDate;
    private boolean emailVerified;

    
    public MemberEntity(MemberJoinDto memberJoinDto) {
		this.email =  memberJoinDto.getEmail();
		this.password =  memberJoinDto.getPassword();
		this.nickname =  memberJoinDto.getNickname();
		this.introduction =  memberJoinDto.getIntroduction();
		this.profileImageId =  memberJoinDto.getProfileImageId();
	}
}
