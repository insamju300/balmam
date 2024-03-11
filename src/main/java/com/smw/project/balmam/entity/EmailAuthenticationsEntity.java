package com.smw.project.balmam.entity;

import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean verified;
    private String type;
    
	public EmailAuthenticationsEntity(Long memberId, String token, LocalDateTime expiresAt) {
		this.memberId = memberId;
		this.token = token;
		this.expiresAt = expiresAt;
	}
    
    
}
