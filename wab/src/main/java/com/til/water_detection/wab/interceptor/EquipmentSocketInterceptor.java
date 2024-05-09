package com.til.water_detection.wab.interceptor;

import com.til.water_detection.wab.config.LoginConfig;
import com.til.water_detection.wab.socket_data.EquipmentSocketContext;
import jakarta.annotation.Resource;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Controller
public class EquipmentSocketInterceptor implements HandshakeInterceptor {

    @Resource
    private LoginConfig loginConfig;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (!(request instanceof ServletServerHttpRequest servletServerHttpRequest)) {
            return false;
        }

        String username = servletServerHttpRequest.getServletRequest().getParameter("username");
        String password = servletServerHttpRequest.getServletRequest().getParameter("password");

        return loginConfig.tryLogin(username, password);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
