package com.example.web_spring.global;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 로컬 폴더를 /uploads/reviews/** URL 로 매핑
        registry.addResourceHandler("/uploads/reviews/**")
                .addResourceLocations("file:" + System.getProperty("user.home") + "/uploads/reviews/");
    }
}
