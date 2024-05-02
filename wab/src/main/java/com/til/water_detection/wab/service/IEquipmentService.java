package com.til.water_detection.wab.service;

import com.til.water_detection.data.Equipment;
import jakarta.annotation.Nullable;

import java.util.List;

public interface IEquipmentService {
    void addEquipment();

    int removeEquipmentPosById(int id);

    List<Equipment> getAllEquipment();

    @Nullable
    Equipment getEquipmentById(int id);

    List<Equipment> getEquipmentByIdArray(int[] id);

    int updateEquipmentAnotherNameById(int id, String anotherName);

    int updateEquipmentTimeById(int id);
}
