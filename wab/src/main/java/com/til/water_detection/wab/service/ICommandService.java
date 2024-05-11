package com.til.water_detection.wab.service;

import com.til.water_detection.data.Command;

import java.util.List;

public interface ICommandService {

    int registerCommand(int ruleId, int actuatorId, int commandTrigger);

    int removeCommandById(int id);

    int updateCommand(int ruleId, Command command);

    Command getCommandById(int id);

    List<Command> getCommandByIdArray(int[] id);

    List<Command> getCommandByActuatorId(int actuatorId);

    List<Command> getCommandByRuleId(int ruleId);

    List<Command> getAllCommands();
}
