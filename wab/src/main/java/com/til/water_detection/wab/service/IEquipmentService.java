package com.til.water_detection.wab.service;

import com.til.water_detection.data.Equipment;
import jakarta.annotation.Nullable;

import java.util.List;

public interface IEquipmentService {
    int registerEquipment(String name);

    int removeEquipmentPosById(int id);

    int updateEquipmentAnotherNameById(int id, String anotherName);

    int updateEquipmentTimeById(int id);

    int updateEquipmentPosById(int id, float longitude, float latitude);

    int updateEquipmentFencePosById(int id,boolean electronicFence, float longitude, float latitude, float range);

    List<Equipment> getAllEquipment();

    @Nullable
    Equipment getEquipmentById(int id);

    @Nullable
    Equipment getEquipmentByName(String name);

    List<Equipment> getEquipmentByIdArray(int[] id);

    List<Equipment> getEquipmentByNameArray(String[] name);
}
