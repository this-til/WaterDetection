package com.til.water_detection.wab.service.impl;

import com.til.water_detection.data.DataType;
import com.til.water_detection.sql.mapper.DataTypeMapper;
import com.til.water_detection.wab.service.DataTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataTypeServiceImpl implements DataTypeService {

    @Autowired
    private DataTypeMapper dataTypeMapper;

    @Override
    public void addDataType(int userId) {
        dataTypeMapper.addDataType(userId);
    }

    @Override
    public List<DataType> getDataTypeListByUserId(int userId) {
        return dataTypeMapper.getDataTypeListByUserId(userId);
    }

    @Override
    public DataType getDatatypeById(int id, int userId) {
        return dataTypeMapper.getDataTypeById(id, userId);
    }

    @Override
    public int updateDataTypeAnotherNameById(int id, int userId, String anotherName) {
        return dataTypeMapper.updateDataTypeAnotherNameById(id, userId, anotherName);
    }
}
