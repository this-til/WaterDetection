package com.til.water_detection.wab.service.impl;

import com.til.water_detection.data.Actuator;
import com.til.water_detection.sql.mapper.IActuatorMapper;
import com.til.water_detection.wab.service.IActuatorService;
import jakarta.annotation.Resource;

import java.util.List;

public class ActuatorServiceImpl implements IActuatorService {

    @Resource
    private IActuatorMapper actuatorMapper;

    @Override
    public int registerActuator(String name) {
        return 0;
    }

    @Override
    public int removeActuatorById(int actuatorId) {
        return 0;
    }

    @Override
    public Actuator getActuatorById(int id) {
        return null;
    }

    @Override
    public Actuator getActuatorByName(String name) {
        return null;
    }

    @Override
    public List<Actuator> getActuatorByIdArray(int[] id) {
        return List.of();
    }

    @Override
    public List<Actuator> getActuatorByNameArray(String[] name) {
        return List.of();
    }

    @Override
    public List<Actuator> getAllActuator() {
        return List.of();
    }
}
