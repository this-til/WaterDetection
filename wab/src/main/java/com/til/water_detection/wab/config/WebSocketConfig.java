package com.til.water_detection.wab.config;

import com.til.water_detection.wab.interceptor.EquipmentSocketInterceptor;
import com.til.water_detection.wab.socket_handler.EquipmentSocketHandler;
import com.til.water_detection.wab.socket_handler.TestHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private EquipmentSocketInterceptor equipmentSocketInterceptor;

    @Resource
    private EquipmentSocketHandler equipmentSocketHandler;

    @Resource
    private TestHandler testHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(equipmentSocketHandler, "/EquipmentSocket")//设置连接路径和处理
                .setAllowedOrigins("*")
                .addInterceptors(equipmentSocketInterceptor);//设置拦截器
        registry.addHandler(testHandler, "/test")
                .setAllowedOrigins("*");
    }


/*    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }*/
}
