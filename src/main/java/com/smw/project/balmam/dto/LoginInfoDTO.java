package com.smw.project.balmam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginInfoDTO {
    private boolean isLogined = false;
    private UserDto userDto = null;
    private String previousUrl = "";

    public LoginInfoDTO(boolean isLogined, UserDto userDto, String previousUrl) {
        this.isLogined = isLogined;
        this.userDto = userDto;
        this.previousUrl = previousUrl;
    }
}
