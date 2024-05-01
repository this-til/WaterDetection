package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;

@Controller("/util")
@ResponseBody
public class UtilController {


    @GetMapping("/timestamp")
    public Result<Timestamp> timestamp() {
        return new Result<>(ResultType.SUCCESSFUL, null, new Timestamp(System.currentTimeMillis()));
    }
}
