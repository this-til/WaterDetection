package com.til.water_detection.wab.interceptor;

import com.til.water_detection.data.util.FinalString;
import com.til.water_detection.wab.config.LoginConfig;
import com.til.water_detection.wab.socket_data.EquipmentSocketContext;
import com.til.water_detection.wab.socket_handler.EquipmentSocketHandler;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Objects;

@Controller
public class EquipmentSocketInterceptor implements HandshakeInterceptor {

    @Resource
    private LoginConfig loginConfig;

    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, @NotNull Map<String, Object> attributes) throws Exception {
        if (!(request instanceof ServletServerHttpRequest servletServerHttpRequest)) {
            return false;
        }
        Map<String, String[]> parameterMap = servletServerHttpRequest.getServletRequest().getParameterMap();
        attributes.put(FinalString.ATTRIBUTES, parameterMap);
        attributes.putAll(parameterMap);

        return loginConfig.tryLogin(parameterMap.get(FinalString.USERNAME)[0], parameterMap.get(FinalString.PASSWORD)[0]);
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, Exception exception) {

    }
}
