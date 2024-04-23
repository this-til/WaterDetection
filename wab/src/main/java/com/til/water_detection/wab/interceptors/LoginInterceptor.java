package com.til.water_detection.wab.interceptors;

import com.til.water_detection.wab.util.FinalString;
import com.til.water_detection.wab.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader(FinalString.AUTHORIZATION);

        try {
            int id = JwtTokenUtil.parseToken(header);
            request.setAttribute(FinalString.ID, id);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }

    }
}
