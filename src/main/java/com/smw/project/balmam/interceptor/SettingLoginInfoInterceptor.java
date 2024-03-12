package com.smw.project.balmam.interceptor;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.smw.project.balmam.dto.LoginInfoDTO;
import com.smw.project.balmam.dto.UserDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class SettingLoginInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        // 로그인 여부 확인
        boolean isLogined = session.getAttribute("userDto") != null;
        

        // 로그인된 경우, UserDto 가져오기
        UserDto userDto = (UserDto)session.getAttribute("userDto");

        // 이전 URL 가져오기
        // 이 부분은 프로젝트의 URL 구조와 어플리케이션 로직에 따라 다르게 구현될 수 있습니다.
        // 예제에서는 단순히 Referer 헤더를 사용합니다.
        String previousUrl = determinePreviousUrl(request);

        // LoginInfoDTO 설정 및 request에 추가
        LoginInfoDTO loginInfo = new LoginInfoDTO(isLogined, userDto, previousUrl);
        request.setAttribute("loginInfo", loginInfo);
        return true;
    }
    
    
    private String determinePreviousUrl(HttpServletRequest request) throws URISyntaxException {
        String refererHeader = request.getHeader("Referer");
        if (refererHeader != null && !refererHeader.isEmpty()) {
            URI refererUri = new URI(refererHeader);
            URI requestUri = new URI(request.getRequestURL().toString());
            if (refererUri.getHost().equals(requestUri.getHost())) {
               return refererUri.getPath();
               
            }
        }
        return null; // 다른 화면에서 넘어오지 않았다면 null 반환
    }
}