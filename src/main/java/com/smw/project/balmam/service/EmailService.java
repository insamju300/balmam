package com.smw.project.balmam.service;




import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.smw.project.balmam.entity.EmailAuthenticationsEntity;
import com.smw.project.balmam.enums.EmailAuthenticationType;
import com.smw.project.balmam.repository.EmailAuthentication;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class EmailService {
	

	@Autowired
    private JavaMailSender mailSender;
	@Autowired
	private EmailAuthentication emailAuthenticationRepository; 
	
    
    // Spring mail username을 불러오는 코드 추가
    @Value("${spring.mail.username}")
    private String fromEmail;


    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        // Properties에서 설정한 email을 setFrom으로 사용
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        
        mailSender.send(message);
    }
    
    @Async
    public void sendEmailVerification(String email, Long memberId, String baseUrl) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24); // 24시간 후 만료
        String type= "emailVerification";

        EmailAuthenticationsEntity emailAuthEntity = new EmailAuthenticationsEntity(memberId, token, expiresAt, EmailAuthenticationType.emailVerification);
        
        emailAuthenticationRepository.insert(emailAuthEntity);
        
       

        String verificationLink = baseUrl + "/member/emailVerification?token=" + token;
        sendSimpleMessage(email, "발맘: 이메일 인증", "다음 링크를 클릭하여 이메일을 인증해주세요: " + verificationLink);
    }


	public void updateVerifiedValue(String token, EmailAuthenticationType type) {
		emailAuthenticationRepository.updateVerifiedValue(token, type);
		
	}

	@Async
	public void sendPasswordRestorationForm(String email, Long memberId, String baseUrl) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24); // 24시간 후 만료

        EmailAuthenticationsEntity emailAuthEntity = new EmailAuthenticationsEntity(memberId, token, expiresAt, EmailAuthenticationType.passwordRestoration);
        
        emailAuthenticationRepository.insert(emailAuthEntity);

        String verificationLink = baseUrl + "/member/passwordRestoration?token=" + token;
        sendSimpleMessage(email, "발맘: 비밀번호 변경", "다음 링크를 클릭하여 비밀번호를 변경해 주세요 " + verificationLink);
        
		
	}

	public EmailAuthenticationsEntity findEmailAuthenticationsFromToken(String token,
			EmailAuthenticationType type) {
		// TODO Auto-generated method stub
		return emailAuthenticationRepository.findEmailAuthenticationsFromToken(token, type);
	}
    
    
}