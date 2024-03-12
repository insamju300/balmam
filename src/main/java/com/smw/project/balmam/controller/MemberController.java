package com.smw.project.balmam.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smw.project.balmam.Enum.EmailAuthenticationType;
import com.smw.project.balmam.dto.LoginInfoDTO;
import com.smw.project.balmam.dto.MemberInputDto;
import com.smw.project.balmam.dto.MemberOutputDto;
import com.smw.project.balmam.dto.MessageResponse;
import com.smw.project.balmam.dto.ResultData;
import com.smw.project.balmam.dto.UserDto;
import com.smw.project.balmam.entity.EmailAuthenticationsEntity;
import com.smw.project.balmam.entity.MemberEntity;
import com.smw.project.balmam.service.EmailService;
import com.smw.project.balmam.service.FileService;
import com.smw.project.balmam.service.MemberService;
import com.smw.project.balmam.utill.Ut;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FileService fileService;
	
	@GetMapping("/member/join")
	public String showJoin(HttpServletRequest req) {
		return "/member/join";
	}
	
	@Value("${file.upload.path}")
	private String path;
	
	
	//회원가입
	@PostMapping("/member/join")
	public String doJoin(MemberInputDto memberInputDto, RedirectAttributes redirectAttributes) {
		MemberEntity memberEntity = new MemberEntity(memberInputDto);
		memberService.insertMember(memberEntity);		
		emailService.sendEmailVerification(memberEntity.getEmail(), memberEntity.getId());
		//todo 로그인시 미인증 계정이면 인증 재전송 필요
		MessageResponse message = new MessageResponse("INFO", String.format("메일 인증 링크를, 설정해주신 이메일(%s)로 전송하였습니다.\n 인증을 완료하시고 멋진 여행을 시작해 주세요.", memberInputDto.getEmail()), "환영합니다.");
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/member/join";
	}
	
	//이메일 변경 메일 전송
	@GetMapping("/member/sendPasswordRestorationForm")
	@ResponseBody
	public ResultData<String> sendPasswordRestorationForm(String email, RedirectAttributes redirectAttributes) {
		System.err.println(email);
		MemberEntity findMember = memberService.findMemberByEmail(email);
		if(findMember == null) {
			ResultData<String> resultData = ResultData.ofMessage("F-1",  String.format("가입되지 않은 email입니다."));	
			return resultData;
		}
		emailService.sendPasswordRestorationForm(email, findMember.getId());
		
		ResultData<String> resultData = ResultData.ofMessage("S-1",  String.format("비밀번호 변경을 위한 링크를 메일(%s)로 전송하였습니다.\n전송된 링크에서 비밀번호를 변경해 주세요.", email));
		return resultData;
	}
	
	//이메일 변경 폼 열기
	@GetMapping("/member/passwordRestoration")
	public String passwordRestoration(RedirectAttributes redirectAttributes, String token, Model model) {
		EmailAuthenticationsEntity emailAuthenticationsEntity = emailService.findEmailAuthenticationsFromToken(token, EmailAuthenticationType.passwordRestoration);
		if(emailAuthenticationsEntity==null) {
			MessageResponse message = new MessageResponse("INFO", "해당하는 리소스가 존재하지 않습니다.", "");
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/";
		}
		
		if(emailAuthenticationsEntity.getExpiresAt().compareTo(LocalDateTime.now()) < 0) {
			MessageResponse message = new MessageResponse("INFO", "이미 만료된 토큰 입니다.", "");
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/";
		}
		
		if(emailAuthenticationsEntity.isVerified()) {
			MessageResponse message = new MessageResponse("INFO", "이미 사용된 토큰 입니다.", "");
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/";
		}
		
		emailService.updateVerifiedValue(token, EmailAuthenticationType.passwordRestoration);
		
		System.err.println(emailAuthenticationsEntity.getMemberId());
		model.addAttribute("id", emailAuthenticationsEntity.getMemberId());
		return "/member/passwordRestoration";
	}
	
	
	//패스워드 변경
	@PostMapping("/member/passwordRestoration")
	public String passwordRestoration(MemberInputDto memberInputDto) {
		MemberEntity memberEntity = new MemberEntity(memberInputDto);
		memberService.updateMember(memberEntity);
		return "redirect:/";
	}
	
	//닉네임 중복 검사
	@GetMapping("/member/isExistsNickname")
	@ResponseBody
	public boolean isExistsNickname(String nickname) {
		return memberService.isExistsNicname(nickname);
	}

	//이메일 중복 검사
	@GetMapping("/member/isExistsEmail")
	@ResponseBody
	public boolean isExistsEmail(String email) {
		return memberService.isExistsEmail(email);
	}
	
	//이메일 인증
	@GetMapping("/member/emailVerification")
	public String emailVerification(String token,RedirectAttributes redirectAttributes ) {
		EmailAuthenticationsEntity emailAuthenticationsEntity = emailService.findEmailAuthenticationsFromToken(token, EmailAuthenticationType.emailVerification);
		if(emailAuthenticationsEntity==null) {
			MessageResponse message = new MessageResponse("INFO", "해당하는 리소스가 존재하지 않습니다.", "");
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/";
		}
		
		if(emailAuthenticationsEntity.getExpiresAt().compareTo(LocalDateTime.now()) < 0) {
			MessageResponse message = new MessageResponse("INFO", "이미 만료된 토큰 입니다.", "");
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/";
		}
		
		emailService.updateVerifiedValue(token, EmailAuthenticationType.emailVerification);
		
		
		MessageResponse message = new MessageResponse("INFO", "메일 인증이 완료되었습니다. \n멋진 여행이 여러분을 기다리길", "환영합니다.");
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/member/login";
	}
	
	//로그인 폼 보여주기
	@GetMapping("/member/login")
	public String showLogin() {

		return "/member/login";
	}
	
	//로그인 처리
	@PostMapping("/member/login")
	public String doLogin(String email, String password, HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes ) {
		LoginInfoDTO loginInfo = (LoginInfoDTO)request.getAttribute("loginInfo");
		if(Ut.isNullOrEmpty(email)) {
			MessageResponse message = new MessageResponse("EXCEPTION", "email이 입력되지 않았습니다.", "잘못된 요청");
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/member/login";
		}
		
		if(Ut.isNullOrEmpty(password)) {
			MessageResponse message = new MessageResponse("EXCEPTION", "비밀번호가 입력되지 않았습니다.", "잘못된 요청");
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/member/login";
		}
		
		MemberEntity findMember = memberService.findMemberByEmail(email);
		
		if (findMember == null) {
			MessageResponse message = new MessageResponse("EXCEPTION", String.format("%s는 존재하지 않는 email입니다.", email), "잘못된 요청");
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/member/login";
		}
		
		if (findMember.isWithdrawn()) {
			MessageResponse message = new MessageResponse("EXCEPTION", "이미 탈퇴한 유저 입니다.", "잘못된 요청");
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/";
		}
		
		if(!memberService.checkPassword(password, findMember)) {
			MessageResponse message = new MessageResponse("EXCEPTION", "비밀번호가 일치하지 않습니다.", "잘못된 요청");
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/member/login";
		}
		
		if(!findMember.isEmailVerified()) {
			MessageResponse message = new MessageResponse("EXCEPTION", "이메일이 인증되지 않은 고객님입니다.", "잘못된 요청");
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/member/login";
		}

		UserDto userDto = new UserDto(findMember, path);
		session.setAttribute("userDto", userDto);
		String previouseUrl = loginInfo.getPreviousUrl();
		if(previouseUrl == null) {
			previouseUrl="/";
		}
		return "redirect:"+previouseUrl;
	}
	
	//로그아웃 처리
	@GetMapping("/member/logout")
	public String doLogout(HttpSession session, RedirectAttributes redirectAttributes ) {
		session.removeAttribute("userDto");
//		String previouseUrl = loginInfo.getPreviousUrl();
//		if(previouseUrl == null) {
//			previouseUrl="/";
//		}
		return "redirect:/";
	}
	
	//탈퇴 처리
	@PostMapping("/member/withdrawn")
	public String doWithdrawn(String password, HttpServletRequest request, RedirectAttributes redirectAttributes ) {
		LoginInfoDTO loginInfo = (LoginInfoDTO)request.getAttribute("loginInfo");
		String email = loginInfo.getUserDto().getEmail();
		MemberEntity findMember = memberService.findMemberByEmail(email);
		
		if(!memberService.checkPassword(password, findMember)) {
			MessageResponse message = new MessageResponse("EXCEPTION", "비밀번호가 일치하지 않습니다.", "잘못된 요청");
			redirectAttributes.addFlashAttribute("message", message);
			String previouseUrl = loginInfo.getPreviousUrl();
			if(previouseUrl == null) {
				previouseUrl="/";
			}
			return "redirect:"+previouseUrl;
		}
		

		memberService.updateWithdrawn(email);
		MessageResponse message = new MessageResponse("INFO", "회원탈퇴가 완료되었습니다.", "안녕히가세요");
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/";
	}
	
	
	@GetMapping("/member/detail")
	public String showDetail(Model model, Long id) {
		
		
		MemberEntity findMember = memberService.findMemberById(id);
		MemberOutputDto member = new MemberOutputDto(findMember, path);
		model.addAttribute("member", member);
		
		return  "/member/detail";
	}	
	
	//수정 폼
	@GetMapping("/member/modify")
	public String showModify(Model model, Long id) {
		
		
		MemberEntity findMember = memberService.findMemberById(id);
		MemberOutputDto member = new MemberOutputDto(findMember, path);
		model.addAttribute("member", member);
		
		return  "modify";
	}
	
	//수정 처리
	@PostMapping("/member/modify")
	public String doModify(MemberInputDto memberInputDto) {
		MemberEntity memberEntity = new MemberEntity(memberInputDto);
		memberService.updateMember(memberEntity);

		return "redirect:/";
	}
	

}
