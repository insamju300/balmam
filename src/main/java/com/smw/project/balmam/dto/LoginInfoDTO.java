package com.smw.project.balmam.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginInfoDTO {
    private boolean logined = false;
    private UserDto userDto = null;
    private Long userId = null;
    private String previousUrl = "";

    public LoginInfoDTO(boolean logined, Long userId, UserDto userDto, String previousUrl) {
        this.logined = logined;
        this.userDto = userDto;
        this.userId = userId;
        this.previousUrl = previousUrl;
    }
}
