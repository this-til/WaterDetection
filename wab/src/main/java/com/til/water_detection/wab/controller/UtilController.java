package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Result;
import com.til.water_detection.data.state.ResultType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("/util")
@Validated
@ResponseBody
public class UtilController {


    @GetMapping("/timestamp")
    public Result<Timestamp> timestamp() {
        return new Result<>(ResultType.SUCCESSFUL, null, new Timestamp(System.currentTimeMillis()));
    }

    @GetMapping("/getIp")
    public Result<String> getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();

            // 这里可以添加额外的逻辑来处理IPv6地址，但通常getRemoteAddr()已经返回正确的值
            // 例如，检查是否以"::"或":"开头（但注意IPv4-mapped IPv6地址）
            // 如果你在负载均衡器或代理后面，上述的X-Forwarded-For等头可能更可靠
        }


        return new Result<>(ResultType.SUCCESSFUL, null, ipAddress);
    }
}
