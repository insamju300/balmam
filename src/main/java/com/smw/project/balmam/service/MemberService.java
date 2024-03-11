package com.smw.project.balmam.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smw.project.balmam.dto.MemberInputDto;
import com.smw.project.balmam.entity.MemberEntity;
import com.smw.project.balmam.repository.MemberRepository;

@Service
public class MemberService {
	@Autowired
	private MemberRepository memberRepository;

	public void insertMember(MemberEntity memberEntity) {
		memberRepository.insertMember(memberEntity);
		
	}

	public boolean isExistsNicname(String nickname) {
		// TODO Auto-generated method stub
		return memberRepository.isExistsNickname(nickname);
	}

	public boolean isExistsEmail(String email) {
		// TODO Auto-generated method stub
		return memberRepository.isExistsEmail(email);
	}
	
    public boolean checkPassword(String inputPassword, MemberEntity memberEntity) {
        try {
            // MessageDigest 인스턴스 생성
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            
            // 입력된 비밀번호를 바이트 배열로 변환하고 해시 계산
            byte[] hashedInputPassword = messageDigest.digest(inputPassword.getBytes());
            
            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedInputPassword) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            // 저장된 해시와 비교
            return memberEntity.getPassword().equals(hexString.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false; // NoSuchAlgorithmException 발생 시 false 반환
        }
    }

	public MemberEntity findMemberByEmail(String email) {
		// TODO Auto-generated method stub
		return memberRepository.findMemberByEmail(email);
	}

	public void updateWithdrawn(String email) {
		// TODO Auto-generated method stub
		memberRepository.updateWithdrawn(email);
	}

	public MemberEntity findMemberById(Long id) {
		// TODO Auto-generated method stub
		return memberRepository.findMemberById(id);
	}

	public void updateMember(MemberInputDto memberInputDto) {
		// TODO Auto-generated method stub
		 memberRepository.updateMember(memberInputDto);
	}


	
	
}
