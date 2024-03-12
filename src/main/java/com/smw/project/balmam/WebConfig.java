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

