package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Result;
import com.til.water_detection.data.state.ResultType;
import com.til.water_detection.data.util.FinalString;
import com.til.water_detection.wab.config.LoginConfig;
import com.til.water_detection.wab.util.Util;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.FileStore;

@RestController
@RequestMapping()
@Validated
@ResponseBody
public class LoginController {

    @Resource
    private LoginConfig loginConfig;

    @PostMapping("/login")
    public Result<String> login(@RequestParam(FinalString.USERNAME) String username, @RequestParam(FinalString.PASSWORD) String password) {
        return loginConfig.tryLogin(username, password) ?  new Result<>(ResultType.SUCCESSFUL, null, Util.generateJwt()) : new Result<>(ResultType.FAIL, "登录失败", null);
    }

}
