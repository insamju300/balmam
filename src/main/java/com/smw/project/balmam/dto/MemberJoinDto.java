package com.smw.project.balmam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinDto {
    private String email;
//    @NotBlank(message = "비밀번호는 생략될 수 없습니다.")
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters long and include at least one digit, one uppercase letter, one lowercase letter, and one special character")
    private String password; // 실제 애플리케이션에서 DTO로 비밀번호를 전송할 때는 주의가 필요합니다.
    private String nickname;
    private String introduction;
    private Long profileImageId;
    
}
