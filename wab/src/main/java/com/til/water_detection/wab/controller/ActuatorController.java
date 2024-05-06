package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Actuator;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import com.til.water_detection.wab.service.IActuatorService;
import com.til.water_detection.wab.util.FinalString;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actuator")
@Validated
@ResponseBody
public class ActuatorController {
    @Resource
    private IActuatorService actuatorService;

    @PostMapping("/registerActuator")
    public Result<Void> registerActuator(@RequestParam @Param(FinalString.VERIFY_1_32) String name) {
        int i = actuatorService.registerActuator(name);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @DeleteMapping("/removeActuatorById")
    public Result<Void> removeActuatorById(@RequestParam int id) {
        int i = actuatorService.removeActuatorById(id);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @GetMapping("/getActuatorById")
    public Result<Actuator> getActuatorById(@RequestParam int id) {
        Actuator actuatorById = actuatorService.getActuatorById(id);
        return new Result<>(actuatorById == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @GetMapping("/getActuatorByName")
    public Result<Actuator> getActuatorByName(@RequestParam String name) {
        Actuator actuatorByName = actuatorService.getActuatorByName(name);
        return new Result<>(actuatorByName == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @GetMapping("/getAllActuator")
    public Result<List<Actuator>> getAllActuator() {
        return new Result<>(ResultType.SUCCESSFUL, null, actuatorService.getAllActuator());
    }

    @GetMapping("/getActuatorByIdArray")

    public Result<List<Actuator>> getActuatorByIdArray(@RequestParam int[] id) {
        return new Result<>(ResultType.SUCCESSFUL, null, actuatorService.getActuatorByIdArray(id));
    }

    @GetMapping("/getActuatorByNameArray")
    public Result<List<Actuator>> getActuatorByNameArray(@RequestParam String[] name) {
        return new Result<>(ResultType.SUCCESSFUL, null, actuatorService.getActuatorByNameArray(name));
    }
}
