package com.til.water_detection.wab.interceptor;

import com.til.water_detection.data.util.FinalString;
import com.til.water_detection.wab.util.Util;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;

@Controller
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final @NotNull HttpServletRequest request, final @NotNull HttpServletResponse response, final @NotNull Object handler) throws Exception {
        String header = request.getHeader(FinalString.TOKEN);
        try {
            Util.parseJwt(header);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
