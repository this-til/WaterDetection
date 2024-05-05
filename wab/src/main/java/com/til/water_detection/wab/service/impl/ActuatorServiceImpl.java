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
        return actuatorMapper.registerActuator(name);
    }

    @Override
    public int removeActuatorById(int id) {
        return actuatorMapper.removeActuatorById(id);
    }

    @Override
    public Actuator getActuatorById(int id) {
        return actuatorMapper.getActuatorById(id);
    }

    @Override
    public Actuator getActuatorByName(String name) {
        return actuatorMapper.getActuatorByName(name);
    }

    @Override
    public List<Actuator> getActuatorByIdArray(int[] id) {
        return actuatorMapper.getActuatorByIdArray(id);
    }

    @Override
    public List<Actuator> getActuatorByNameArray(String[] name) {
        return actuatorMapper.getActuatorByNameArray(name);
    }

    @Override
    public List<Actuator> getAllActuator() {
        return List.of();
    }
}
