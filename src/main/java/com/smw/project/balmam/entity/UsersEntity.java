package com.smw.project.balmam.entity;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class UsersEntity {
    private Long id;
    private String email;
    private String password; // 실제 애플리케이션에서 DTO로 비밀번호를 전송할 때는 주의가 필요합니다.
    private String nickname;
    private String introduction;
    private Long profileImageId;
    private String profileImageUrl;
    private Boolean isWithdrawn;
    private LocalDateTime withdrawalDate;
    private Boolean emailVerified;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
