package com.til.water_detection.wab.service;

import com.til.water_detection.data.DataType;
import jakarta.annotation.Nullable;

import java.util.List;

public interface DataTypeService {
    void addDataType(int userId);
    List<DataType> getDataTypeListByUserId(int userId);

    @Nullable
    DataType getDatatypeById(int id, int userId);

    void updateDataTypeAnotherNameById(int id,int userId, String anotherName);
}
