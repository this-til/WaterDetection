package com.til.water_detection.wab;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@ComponentScan("com.til.water_detection")
@MapperScan("com.til.water_detection.sql.mapper")
@EnableWebSocket
public class WabApplication {

    public static void main(String[] args) {
        SpringApplication.run(WabApplication.class, args);
    }

}
