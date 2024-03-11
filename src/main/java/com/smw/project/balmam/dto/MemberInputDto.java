package com.smw.project.balmam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInputDto {
	private Long id;
    private String email;
    private String password;
    private String nickname;
    private String introduction;
    private Long profileImageId;
    
}
