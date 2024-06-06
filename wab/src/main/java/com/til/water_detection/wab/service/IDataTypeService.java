package com.til.water_detection.wab.service;

import com.til.water_detection.data.DataType;
import jakarta.annotation.Nullable;

import java.util.List;

public interface IDataTypeService {
    int registerDataType(String name);

    int removeDataTypeById(int id);

    int updateDataTypeAnotherName(int id, String anotherName);

    int updateDataTypeSuffixById(int id, String suffix);

    int updateDataTypePercentById(int id, boolean percent);


    @Nullable
    DataType getDataTypeById(int id);


    @Nullable
    DataType getDataTypeByName(String name);

    List<DataType> getDataTypeByIdArray(int[] id);

    List<DataType> getAllDataType();


    List<DataType> getDataTypeByNameArray(String[] name);
}
