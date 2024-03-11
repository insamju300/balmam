package com.smw.project.balmam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MemberController {
	
	@GetMapping("/member/join")
	public String showJoin(HttpServletRequest req) {
		return "/member/join";
	}
//  @PostMapping("/join")
//  public String registerUser(@Valid UsersJoinDto usersJoinDto, BindingResult result, RedirectAttributes redirectAttributes) {
//      if (result.hasErrors()) {
//          // If validation fails, stay on the join form page
//          return "joinForm"; // Name of your join form view
//      }
//
//      // Your registration logic here...
//
//      redirectAttributes.addFlashAttribute("successMessage", "Registration successful!");
//      return "redirect:/joinSuccess"; // Redirect on successful registration
//  }
//	<!DOCTYPE html>
//	<html xmlns:th="http://www.thymeleaf.org">
//	<head>
//	    <meta charset="UTF-8">
//	    <title>Join Membership</title>
//	</head>
//	<body>
//	<h1>Join</h1>
//	<form th:action="@{/join}" th:object="${usersJoinDto}" method="post">
//	    <!-- Email Field -->
//	    <div>
//	        <label for="email">Email:</label>
//	        <input id="email" type="email" th:field="*{email}">
//	        <p th:if="${#fields.hasErrors('email')}" th:errors="*{email}"></p>
//	    </div>
//
//	    <!-- Password Field -->
//	    <div>
//	        <label for="password">Password:</label>
//	        <input id="password" type="password" th:field="*{password}">
//	        <p th:if="${#fields.hasErrors('password')}" th:errors="*{password}"></p>
//	    </div>
//
//	    <!-- Other fields... -->
//
//	    <button type="submit">Submit</button>
//	</form>
//	</body>
//	</html>
	
	//@GetMapping("/memberEmailDuplicate")
	//@RequestBody
	
	
	
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
