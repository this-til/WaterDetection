package com.til.water_detection.wab.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginConfig {

    @Value("${user.username}")
    private String username;

    @Value("${user.password}")
    private String password;


    public boolean tryLogin(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}
