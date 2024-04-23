package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import com.til.water_detection.data.User;
import com.til.water_detection.data.Util;
import com.til.water_detection.wab.service.UserService;
import com.til.water_detection.wab.util.FinalString;
import com.til.water_detection.wab.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@Validated
public class UserController {


    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public Result<Void> register(@Param(FinalString.VERIFY_5_30) String username, @Param(FinalString.VERIFY_5_30) String password) {
        User user = userService.getUser(username);
        if (user == null) {
            return Result.fail("用户名已被注册");
        }
        userService.register(username, password);
        return Result.successful(null);
    }

    @GetMapping("/login")
    public Result<String> login(@Param(FinalString.VERIFY_5_30) String username, @Param(FinalString.VERIFY_5_30) String password) {
        User user = userService.getUser(username);
        if (user == null) {
            return Result.fail("未知的用户");
        }
        if (!user.getPassword().equals(Util.hashPassword(password, user.getSalt()))) {
            return Result.fail("密码错误");
        }
        String token = JwtTokenUtil.genToken(user);
        return Result.successful(token);
    }

    @GetMapping("/info")
    public Result<User> userInfo(HttpServletRequest request) {

        User user = userService.getUser((int) request.getAttribute(FinalString.ID));
        user.setPassword("");
        user.setSalt("");

        return new Result<>(ResultType.SUCCESSFUL, null, user);
    }


}
