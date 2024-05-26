package com.til.water_detection.wab.config;

import com.til.water_detection.wab.interceptor.LoginInterceptor;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Value("${user.username}")
    private String username;

    @Value("${user.password}")
    private String password;

    @Resource
    private LoginInterceptor loginInterceptor;

    public boolean tryLogin(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/login");
    }
}
