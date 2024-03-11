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

import com.smw.project.balmam.dto.LoginInfoDTO;
import com.smw.project.balmam.dto.MemberInputDto;
import com.smw.project.balmam.dto.MemberOutputDto;
import com.smw.project.balmam.dto.MessageResponse;
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
	
	
	@PostMapping("/member/join")
	public String doJoin(MemberInputDto memberInputDto, RedirectAttributes redirectAttributes) {
		MemberEntity memberEntity = new MemberEntity(memberInputDto);
		memberService.insertMember(memberEntity);		
		emailService.sendEmailVerification(memberEntity.getEmail(), memberEntity.getId());
		//todo 로그인시 미인증 계정이면 인증 재전송 필요
		MessageResponse message = new MessageResponse("INFO", String.format("비밀번호 변경을 위한 링크를 메일(%s)로 전송하였습니다.\n전송된 링크에서 비밀번호를 변경해 주세요.", memberInputDto.getEmail()), "메일을 전송하였습니다.");
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/member/join";
	}
	
	
	@GetMapping("/member/isExistsNickname")
	@ResponseBody
	public boolean isExistsNickname(String nickname) {
		return memberService.isExistsNicname(nickname);
	}

	@GetMapping("/member/isExistsEmail")
	@ResponseBody
	public boolean isExistsEmail(String email) {
		return memberService.isExistsEmail(email);
	}
	
	@GetMapping("/member/emailVerification")
	public String emailVerification(String token,RedirectAttributes redirectAttributes ) {
		EmailAuthenticationsEntity emailAuthenticationsEntity = emailService.findEmailAuthenticationsFromToken(token);
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
		
		emailService.updateVerifiedValue("token");
		
		
		MessageResponse message = new MessageResponse("INFO", "메일 인증이 완료되었습니다. \n멋진 여행이 여러분을 기다리길", "환영합니다.");
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/member/login";
	}
	
	@GetMapping("/member/login")
	public String showLogin() {

		return "/member/login";
	}
	
	@PostMapping("/member/login")
	public String doLogin(String email, String password, HttpSession session, LoginInfoDTO loginInfo, RedirectAttributes redirectAttributes ) {
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
		session.setAttribute("userId", findMember.getId());
		String previouseUrl = loginInfo.getPreviousUrl();
		if(previouseUrl == null) {
			previouseUrl="/";
		}
		return "redirect:"+previouseUrl;
	}
	
	@GetMapping("/member/logout")
	public String doLogout(HttpSession session, LoginInfoDTO loginInfo, RedirectAttributes redirectAttributes ) {
		session.removeAttribute("userDto");
		session.removeAttribute("userId");
		String previouseUrl = loginInfo.getPreviousUrl();
		if(previouseUrl == null) {
			previouseUrl="/";
		}
		return "redirect:"+previouseUrl;
	}
	
	@PostMapping("/member/withdrawn")
	public String doWithdrawn(String password, LoginInfoDTO loginInfo, RedirectAttributes redirectAttributes ) {
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
	
	@GetMapping("/member/modify")
	public String showModify(Model model, Long id) {
		
		
		MemberEntity findMember = memberService.findMemberById(id);
		MemberOutputDto member = new MemberOutputDto(findMember, path);
		model.addAttribute("member", member);
		
		return  "modify";
	}
	
	@PostMapping("/member/modify")
	public String doModify(MemberInputDto memberInputDto) {
		MemberEntity memberEntity = new MemberEntity(memberInputDto);
		memberService.updateMember(memberInputDto);

		return "redirect:/";
	}
	
	
	
	
	//@GetMapping("/member/modify")
	
	
	
	
	
	//todo email nickname 중복 검증
	
	
//    @GetMapping("/join")
//    public String showRegistrationForm(UsersJoinDto usersJoinDto) {
//        return "joinForm"; // Name of your join form view
//    }
//
//    @PostMapping("/join")
//    public String registerUser(@Valid UsersJoinDto usersJoinDto, BindingResult result, RedirectAttributes redirectAttributes) {
//        if (result.hasErrors()) {
//            // If validation fails, stay on the join form page
//            return "joinForm"; // Name of your join form view
//        }
//
//        // Your registration logic here...
//
//        redirectAttributes.addFlashAttribute("successMessage", "Registration successful!");
//        return "redirect:/joinSuccess"; // Redirect on successful registration
//    }
//
	
	

//	@Autowired
//	private Rq rq;
//
//	@Autowired
//	private MemberService memberService;
//
//	@RequestMapping("/usr/member/getLoginIdDup")
//	@ResponseBody
//	public ResultData getLoginIdDup(String loginId) {
//
//		if (Ut.isEmpty(loginId)) {
//			return ResultData.from("F-1", "아이디를 입력해주세요");
//		}
//
//		Member existsMember = memberService.getMemberByLoginId(loginId);
//
//		if (existsMember != null) {
//			return ResultData.from("F-2", "해당 아이디는 이미 사용중이야", "loginId", loginId);
//		}
//
//		return ResultData.from("S-1", "사용 가능!", "loginId", loginId);
//	}
//
//	@RequestMapping("/usr/member/doLogout")
//	@ResponseBody
//	public String doLogout(HttpServletRequest req, @RequestParam(defaultValue = "/") String afterLogoutUri) {
//		Rq rq = (Rq) req.getAttribute("rq");
//
//		if (!rq.isLogined()) {
//			return Ut.jsHistoryBack("F-A", "이미 로그아웃 상태입니다");
//		}
//
//		rq.logout();
//
//		return Ut.jsReplace("S-1", "로그아웃 되었습니다", afterLogoutUri);
//	}
//
//	@RequestMapping("/usr/member/login")
//	public String showLogin(HttpServletRequest req) {
//
//		Rq rq = (Rq) req.getAttribute("rq");
//
//		if (rq.isLogined()) {
//			return Ut.jsHistoryBack("F-A", "이미 로그인 함");
//		}
//
//		return "usr/member/login";
//	}
//
//	@RequestMapping("/usr/member/doLogin")
//	@ResponseBody
//	public String doLogin(HttpServletRequest req, String loginId, String loginPw,
//			@RequestParam(defaultValue = "/") String afterLoginUri) {
//
//		Rq rq = (Rq) req.getAttribute("rq");
//
//		if (rq.isLogined()) {
//			return Ut.jsHistoryBack("F-A", "이미 로그인 함");
//		}
//
//		if (Ut.isNullOrEmpty(loginId)) {
//			return Ut.jsHistoryBack("F-1", "아이디를 입력해주세요");
//		}
//		if (Ut.isNullOrEmpty(loginPw)) {
//			return Ut.jsHistoryBack("F-2", "비밀번호를 입력해주세요");
//		}
//
//		Member member = memberService.getMemberByLoginId(loginId);
//
//		if (member == null) {
//			return Ut.jsHistoryBack("F-3", Ut.f("%s(은)는 존재하지 않는 아이디입니다", loginId));
//		}
//
//		if (member.getLoginPw().equals(loginPw) == false) {
//			return Ut.jsHistoryBack("F-4", Ut.f("비밀번호가 일치하지 않습니다"));
//		}
//
//		rq.login(member);
//
//		if (afterLoginUri.length() > 0) {
//			return Ut.jsReplace("S-1", Ut.f("%s님 환영합니다", member.getNickname()), afterLoginUri);
//		}
//
//		return Ut.jsReplace("S-1", Ut.f("%s님 환영합니다", member.getNickname()), "/");
//	}
//
//	@RequestMapping("/usr/member/join")
//	public String showJoin(HttpServletRequest req) {
//
//		return "usr/member/join";
//	}
//
//	@RequestMapping("/usr/member/doJoin")
//	@ResponseBody
//	public String doJoin(HttpServletRequest req, String loginId, String loginPw, String name, String nickname,
//			String cellphoneNum, String email) {
//		Rq rq = (Rq) req.getAttribute("rq");
//		if (rq.isLogined()) {
//			return Ut.jsHistoryBack("F-A", "이미 로그인 상태입니다");
//		}
//
//		if (Ut.isNullOrEmpty(loginId)) {
//			return Ut.jsHistoryBack("F-1", "아이디를 입력해주세요");
//		}
//		if (Ut.isNullOrEmpty(loginPw)) {
//			return Ut.jsHistoryBack("F-2", "비밀번호를 입력해주세요");
//		}
//		if (Ut.isNullOrEmpty(name)) {
//			return Ut.jsHistoryBack("F-3", "이름을 입력해주세요");
//		}
//		if (Ut.isNullOrEmpty(nickname)) {
//			return Ut.jsHistoryBack("F-4", "닉네임을 입력해주세요");
//		}
//		if (Ut.isNullOrEmpty(cellphoneNum)) {
//			return Ut.jsHistoryBack("F-5", "전화번호를 입력해주세요");
//
//		}
//		if (Ut.isNullOrEmpty(email)) {
//			return Ut.jsHistoryBack("F-6", "이메일을 입력해주세요");
//		}
//
//		ResultData<Integer> joinRd = memberService.join(loginId, loginPw, name, nickname, cellphoneNum, email);
//
//		if (joinRd.isFail()) {
//			return Ut.jsHistoryBack(joinRd.getResultCode(), joinRd.getMsg());
//		}
//
//		Member member = memberService.getMember(joinRd.getData1());
//
//		return Ut.jsReplace(joinRd.getResultCode(), joinRd.getMsg(), "../member/login");
//	}
//
//	@RequestMapping("/usr/member/myPage")
//	public String showMyPage() {
//
//		return "usr/member/myPage";
//	}
//
//	@RequestMapping("/usr/member/checkPw")
//	public String showCheckPw() {
//
//		return "usr/member/checkPw";
//	}
//
//	@RequestMapping("/usr/member/doCheckPw")
//	public String doCheckPw(String loginPw) {
//
//		if (Ut.isNullOrEmpty(loginPw)) {
//			return rq.historyBackOnView("비번 입력해");
//		}
//
//		if (rq.getLoginedMember().getLoginPw().equals(loginPw) == false) {
//			return rq.historyBackOnView("비번 틀림");
//		}
//
//		return "usr/member/modify";
//	}
//
//	@RequestMapping("/usr/member/doModify")
//	@ResponseBody
//	public String doModify(HttpServletRequest req, String loginPw, String name, String nickname, String cellphoneNum,
//			String email) {
//		Rq rq = (Rq) req.getAttribute("rq");
//
//		// 비밀번호 안바꿀 수도 있어서 비번 null 체크는 제거
//
//		if (Ut.isNullOrEmpty(name)) {
//			return Ut.jsHistoryBack("F-3", "이름을 입력해주세요");
//		}
//		if (Ut.isNullOrEmpty(nickname)) {
//			return Ut.jsHistoryBack("F-4", "닉네임을 입력해주세요");
//		}
//		if (Ut.isNullOrEmpty(cellphoneNum)) {
//			return Ut.jsHistoryBack("F-5", "전화번호를 입력해주세요");
//
//		}
//		if (Ut.isNullOrEmpty(email)) {
//			return Ut.jsHistoryBack("F-6", "이메일을 입력해주세요");
//		}
//
//		ResultData modifyRd;
//
//		if (Ut.isNullOrEmpty(loginPw)) {
//			modifyRd = memberService.modifyWithoutPw(rq.getLoginedMemberId(), name, nickname, cellphoneNum, email);
//		} else {
//			modifyRd = memberService.modify(rq.getLoginedMemberId(), loginPw, name, nickname, cellphoneNum, email);
//		}
//
//		return Ut.jsReplace(modifyRd.getResultCode(), modifyRd.getMsg(), "../member/myPage");
//	}
}
