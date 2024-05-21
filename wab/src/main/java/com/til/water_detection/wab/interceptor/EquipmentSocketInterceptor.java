package com.til.water_detection.wab.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.til.water_detection.data.run_time.LoginData;
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
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@Controller
public class EquipmentSocketInterceptor implements HandshakeInterceptor {

    @Resource
    private LoginConfig loginConfig;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, @NotNull Map<String, Object> attributes) throws Exception {
        if (!(request instanceof ServletServerHttpRequest servletServerHttpRequest)) {
            return false;
        }
        URI uri = request.getURI();
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
        String loginData = queryParams.getFirst(FinalString.LOGIN_DATA);
        // Object _loginData = servletServerHttpRequest.getServletRequest().getAttribute(FinalString.LOGIN_DATA);
        // String loginData = _loginData.toString();
        byte[] decode = Base64.getDecoder().decode(loginData);
        String decodeLoginData = new String(decode, StandardCharsets.UTF_8);
        LoginData __loginData = objectMapper.readValue(decodeLoginData, LoginData.class);
        attributes.put(FinalString.LOGIN_DATA, __loginData);
        return loginConfig.tryLogin(__loginData.getUsername(), __loginData.getPassword());
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, Exception exception) {

    }
}
