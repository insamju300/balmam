package com.smw.project.balmam.entity;

import java.time.LocalDateTime;

import com.smw.project.balmam.Enum.EmailAuthenticationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthenticationsEntity {
    private Long id;
    private Long memberId;
    private String token;
    private LocalDateTime regDate;
    private LocalDateTime expiresAt;
    private boolean verified;
    EmailAuthenticationType type;
    
	public EmailAuthenticationsEntity(Long memberId, String token, LocalDateTime expiresAt) {
		this.memberId = memberId;
		this.token = token;
		this.expiresAt = expiresAt;
	}

	public EmailAuthenticationsEntity(Long memberId, String token, LocalDateTime expiresAt,
			EmailAuthenticationType type) {
		// TODO Auto-generated constructor stub
		this.memberId = memberId;
		this.token = token;
		this.expiresAt = expiresAt;
		this.type = type;
	}
    
    
}
