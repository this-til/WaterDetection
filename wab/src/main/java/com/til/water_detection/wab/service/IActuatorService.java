package com.til.water_detection.wab.service;

import com.til.water_detection.data.Actuator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IActuatorService {

    int registerActuator(String name);

    int removeActuatorById(int actuatorId);

    Actuator getActuatorById(int id);

    Actuator getActuatorByName(String name);

    List<Actuator> getActuatorByIdArray(int[] id);

    List<Actuator> getActuatorByNameArray(String[] name);

    List<Actuator> getAllActuator();
}
