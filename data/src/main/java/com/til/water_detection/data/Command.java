package com.til.water_detection.data;

import java.util.Objects;

public class Command {

    private int id;
    private int ruleId;
    private int actuatorId;
    private int commandTrigger;

    public Command(int id, int ruleId, int actuatorId, int commandTrigger) {
        this.id = id;
        this.ruleId = ruleId;
        this.actuatorId = actuatorId;
        this.commandTrigger = commandTrigger;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public int getActuatorId() {
        return actuatorId;
    }

    public void setActuatorId(int actuatorId) {
        this.actuatorId = actuatorId;
    }

    public int getCommandTrigger() {
        return commandTrigger;
    }

    public void setCommandTrigger(int commandTrigger) {
        this.commandTrigger = commandTrigger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return id == command.id && ruleId == command.ruleId && actuatorId == command.actuatorId && commandTrigger == command.commandTrigger;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruleId, actuatorId, commandTrigger);
    }

    @Override
    public String toString() {
        return "Command{" +
                "id=" + id +
                ", ruleId=" + ruleId +
                ", actuatorId=" + actuatorId +
                ", commandTrigger=" + commandTrigger +
                '}';
    }
}
