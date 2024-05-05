package com.til.water_detection.wab.service.impl;

import com.til.water_detection.data.Equipment;
import com.til.water_detection.sql.mapper.IEquipmentMapper;
import com.til.water_detection.wab.service.IEquipmentService;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentIdServiceImpl implements IEquipmentService {

    @Resource
    private IEquipmentMapper equipmentMapper;

    @Override
    public int registerEquipment(String name) {
        return equipmentMapper.registerEquipment(name);
    }

    @Override
    public int removeEquipmentPosById(int id) {
        return equipmentMapper.removeEquipmentPosById(id);
    }

    @Override
    public int updateEquipmentAnotherNameById(int id, String anotherName) {
        return equipmentMapper.updateEquipmentAnotherName(id, anotherName);
    }

    @Override
    public int updateEquipmentTimeById(int id) {
        return equipmentMapper.updateEquipmentTimeById(id);
    }

    @Override
    public Equipment getEquipmentById(int id) {
        return equipmentMapper.getEquipmentById(id);
    }

    @Nullable
    @Override
    public Equipment getEquipmentByName(String name) {
        return equipmentMapper.getEquipmentByName(name);
    }

    @Override
    public List<Equipment> getAllEquipment() {
        return equipmentMapper.getAllEquipment();
    }

    @Override
    public List<Equipment> getEquipmentByIdArray(int[] id) {
        return equipmentMapper.getEquipmentByIdArray(id);
    }

    @Override
    public List<Equipment> getEquipmentByNameArray(String[] name) {
        return equipmentMapper.getEquipmentByNameArray(name);
    }

}
