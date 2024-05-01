package com.til.water_detection.wab.service;

import com.til.water_detection.data.DataType;
import jakarta.annotation.Nullable;

import java.util.List;

public interface IDataTypeService {
    void addDataType();

    int removeDataTypeById(int id);

    List<DataType> getAllDataType();
    @Nullable
    DataType getDataTypeById(int id);
    int updateDataTypeAnotherName(int id, String anotherName);
}
