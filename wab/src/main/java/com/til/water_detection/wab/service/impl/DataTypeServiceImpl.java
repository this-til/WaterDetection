package com.til.water_detection.wab.service.impl;

import com.til.water_detection.data.DataType;
import com.til.water_detection.sql.mapper.IDataTypeMapper;
import com.til.water_detection.wab.service.IDataTypeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataTypeServiceImpl implements IDataTypeService {

    @Resource
    private IDataTypeMapper dataTypeMapper;

    @Override
    public int registerDataType(String name) {
        return dataTypeMapper.registerDataType(name);
    }

    @Override
    public int removeDataTypeById(int id) {
        return dataTypeMapper.removeDataTypeById(id);
    }

    @Override
    public int updateDataTypeAnotherName(int id, String anotherName) {
        return dataTypeMapper.updateDataTypeAnotherNameById(id, anotherName);
    }

    @Override
    public DataType getDataTypeById(int id) {
        return dataTypeMapper.getDataTypeById(id);
    }

    @Override
    public DataType getDataTypeByName(String name) {
        return dataTypeMapper.getDataTypeByName(name);
    }

    @Override
    public List<DataType> getAllDataType() {
        return dataTypeMapper.getAllDataType();
    }

    @Override
    public List<DataType> getDataTypeByIdArray(int[] id) {
        return dataTypeMapper.getDataTypeByIdArray(id);
    }

    @Override
    public List<DataType> getDataTypeByNameArray(String[] name) {
        return dataTypeMapper.getDataTypeByNameArray(name);
    }
}
