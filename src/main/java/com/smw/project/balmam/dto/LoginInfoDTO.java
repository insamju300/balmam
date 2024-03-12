package com.smw.project.balmam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginInfoDTO {
    private boolean logined = false;
    private UserDto userDto = null;
    private String previousUrl = "";

    public LoginInfoDTO(boolean logined, UserDto userDto, String previousUrl) {
        this.logined = logined;
        this.userDto = userDto;
        this.previousUrl = previousUrl;
    }
}
