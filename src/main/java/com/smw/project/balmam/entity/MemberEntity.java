package com.smw.project.balmam.entity;

import java.time.LocalDateTime;

import com.smw.project.balmam.dto.MemberInputDto;

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
    
    private String extra__profileImageName;

    
    public MemberEntity(MemberInputDto memberInputDto) {
    	this.id = memberInputDto.getId();
		this.email =  memberInputDto.getEmail();
		this.password =  memberInputDto.getPassword();
		this.nickname =  memberInputDto.getNickname();
		this.introduction =  memberInputDto.getIntroduction();
		this.profileImageId =  memberInputDto.getProfileImageId();
	}
}
