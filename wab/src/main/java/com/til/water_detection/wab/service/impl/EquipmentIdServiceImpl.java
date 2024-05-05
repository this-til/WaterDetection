package com.til.water_detection.wab.service.impl;

import com.til.water_detection.data.Equipment;
import com.til.water_detection.sql.mapper.IEquipmentMapper;
import com.til.water_detection.wab.service.IEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentIdServiceImpl implements IEquipmentService {

    @Autowired
    private IEquipmentMapper equipmentMapper;

    @Override
    public void addEquipment() {
        equipmentMapper.addEquipment();
    }

    @Override
    public int removeEquipmentPosById(int id) {
        return equipmentMapper.removeEquipmentPosById(id);
    }

    @Override
    public List<Equipment> getAllEquipment() {
        return equipmentMapper.getAllEquipment();
    }

    @Override
    public Equipment getEquipmentById(int id) {
        return equipmentMapper.getEquipmentById(id);
    }

    @Override
    public List<Equipment> getEquipmentByIdArray(int[] id) {
        return equipmentMapper.getEquipmentByIdArray( id);
    }

    @Override
    public int updateEquipmentAnotherNameById(int id, String anotherName) {
        return equipmentMapper.updateEquipmentAnotherName(id, anotherName);
    }

    @Override
    public int updateEquipmentTimeById(int id) {
        return equipmentMapper.updateEquipmentTimeById(id);
    }
}
