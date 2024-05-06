package com.til.water_detection.wab.service.impl;

import com.til.water_detection.data.Command;
import com.til.water_detection.sql.mapper.ICommandMapper;
import com.til.water_detection.wab.service.ICommandService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandServiceImpl implements ICommandService {

    @Resource
    private ICommandMapper commandMapper;

    @Override
    public int registerCommand(int ruleId, int actuatorId, int commandTrigger) {
        return commandMapper.registerCommand(ruleId, actuatorId, commandTrigger);
    }

    @Override
    public int removeCommandById(int id) {
        return commandMapper.removeCommandById(id);
    }

    @Override
    public Command getCommandById(int id) {
        return commandMapper.getCommandById(id);
    }

    @Override
    public List<Command> getCommandByIdArray(int[] id) {
        return commandMapper.getCommandByIdArray(id);
    }

    @Override
    public List<Command> getCommandByActuatorId(int actuatorId) {
        return commandMapper.getCommandByActuatorId(actuatorId);
    }

    @Override
    public List<Command> getCommandByRuleId(int ruleId) {
        return commandMapper.getCommandByRuleId(ruleId);
    }

    @Override
    public List<Command> getAllCommands() {
        return commandMapper.getAllCommands();
    }
}
