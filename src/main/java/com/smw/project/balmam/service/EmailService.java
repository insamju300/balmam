//package com.smw.project.balmam.service;
//
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//    @Autowired
//    private EmailAuthenticationRepository emailAuthRepo;
//
//    // 이메일 인증 토큰 생성 및 전송
//    public void sendVerificationEmail(User user) {
//        String token = UUID.randomUUID().toString();
//        EmailAuthentication emailAuth = new EmailAuthentication();
//        emailAuth.setUserId(user.getId());
//        emailAuth.setToken(token);
//        emailAuth.setCreatedAt(new Date());
//        emailAuth.setExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // 24시간 후 만료
//        emailAuth.setVerified(false);
//        emailAuthRepo.save(emailAuth);
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(user.getEmail());
//        message.setSubject("이메일 인증");
//        message.setText("이메일 인증을 완료하려면 다음 링크를 클릭하세요: " + 
//                        "http://localhost:8080/verify?token=" + token);
//        mailSender.send(message);
//    }
//
//    // 이메일 인증 토큰 검증
//    public boolean verifyEmail(String token) {
//        EmailAuthentication emailAuth = emailAuthRepo.findByToken(token);
//        if (emailAuth != null && !emailAuth.isVerified() && emailAuth.getExpiresAt().after(new Date())) {
//            emailAuth.setVerified(true);
//            emailAuthRepo.save(emailAuth);
//            return true;
//        }
//        return false;
//    }
//}