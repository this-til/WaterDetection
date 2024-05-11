package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Command;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import com.til.water_detection.wab.service.ICommandService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/command")
@Validated
@ResponseBody
public class CommandController {

    @Resource
    private ICommandService commandService;

    @PostMapping("/registerCommand")
    public Result<Void> registerCommand(@RequestParam int ruleId, @RequestParam int actuatorId, @RequestParam int commandTrigger) {
        int i = commandService.registerCommand(ruleId, actuatorId, commandTrigger);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @DeleteMapping("/removeCommandById")
    public Result<Void> removeCommandById(@RequestParam int id) {
        int i = commandService.removeCommandById(id);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @PutMapping("/updateCommandById")
    public Result<Void> updateCommandById(@RequestParam int id, @RequestBody Command command) {
        int i = commandService.updateCommand(id, command);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @GetMapping("/getCommandById")
    public Result<Command> getCommandById(@RequestParam int id) {
        Command commandById = commandService.getCommandById(id);
        return new Result<>(commandById == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, commandById);
    }

    @GetMapping("/getCommandByIdArray")
    public Result<List<Command>> getCommandByIdArray(@RequestParam int[] id) {
        List<Command> commandByIdArray = commandService.getCommandByIdArray(id);
        return new Result<>(ResultType.SUCCESSFUL, null, commandByIdArray);
    }

    @GetMapping("/getCommandByActuatorId")
    public Result<List<Command>> getCommandByActuatorId(@RequestParam int actuatorId) {
        List<Command> commandByActuatorId = commandService.getCommandByActuatorId(actuatorId);
        return new Result<>(ResultType.SUCCESSFUL, null, commandByActuatorId);
    }

    @GetMapping("/getCommandByRuleId")
    public Result<List<Command>> getCommandByRuleId(@RequestParam int ruleId) {
        List<Command> commandByRuleId = commandService.getCommandByRuleId(ruleId);
        return new Result<>(ResultType.SUCCESSFUL, null, commandByRuleId);
    }

    @GetMapping("/getAllCommands")
    public Result<List<Command>> getAllCommands() {
        List<Command> commands = commandService.getAllCommands();
        return new Result<>(ResultType.SUCCESSFUL, null, commands);
    }

}
