package com.smw.project.balmam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.smw.project.balmam.interceptor.SettingLoginInfoInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Value("${file.upload.realPath}")
	private String uploadRealPath;
	
	@Value("${file.upload.path}")
	private String uploadPath;
	
    @Autowired
    private SettingLoginInfoInterceptor settingLoginInfoInterceptor;

	
    // 특정 패턴(uploadPath+"/**")에 매칭되는 URL로 들어오는 요청을 처리할 리소스 핸들러
    // ddResourceLocations("file:///"+uploadRealPath) 실제 리소스가 위치하는 경로를 설정
    //웹 URL 경로와 물리적 서버 위치 간의 연결(또는 매핑)을 생성. 이 "파이프"는 각 요청마다 생성되거나 삭제되지 않는다
    //캐싱: 클라이언트 브라우저에 리소스 사본을 저장하고 캐시 유효 기간 내에 있는 한 후속 요청에 이를 재사용할 수 있음을 알려준다.
    //표시된 구성은 웹 요청 경로를 기반으로 서버의 파일 시스템에서 정적 리소스를 효율적으로 제공하기 위한 시스템을 설정하며, 리소스 체인 개념은 해당 리소스가 처리되고 전달되는 방식을 최적화하는 것.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(uploadPath+"/**")
                .addResourceLocations("file:///"+uploadRealPath)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
        .addInterceptor(settingLoginInfoInterceptor)
        .addPathPatterns("/**")
        .order(Integer.MIN_VALUE);;
    }
}

