package com.smw.project.balmam.dto;

import com.smw.project.balmam.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
	public UserDto(MemberEntity findMember, String path) {
		this.id = findMember.getId();
		this.email = findMember.getEmail();
		this.nickname = findMember.getNickname();
		this.profileImageUrl = path+"/"+findMember.getExtra__profileImageName();
	}
	private Long id;
    private String email;
    private String nickname;
    private String profileImageUrl;
}
