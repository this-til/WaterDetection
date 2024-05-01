package com.til.water_detection.wab.service.impl;

import com.til.water_detection.data.DataType;
import com.til.water_detection.sql.mapper.DataTypeMapper;
import com.til.water_detection.wab.service.IDataTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataTypeServiceImpl implements IDataTypeService {

    @Autowired
    private DataTypeMapper dataTypeMapper;

    @Override
    public void addDataType() {
        dataTypeMapper.addDataType();
    }

    @Override
    public int removeDataTypeById(int id){
        return dataTypeMapper.removeDataTypeById(id);
    }

    @Override
    public List<DataType> getAllDataType() {
        return dataTypeMapper.getAllDataType();
    }

    @Override
    public DataType getDataTypeById(int id) {
        return dataTypeMapper.getDataTypeById(id);
    }

    @Override
    public int updateDataTypeAnotherName(int id, String anotherName) {
        return dataTypeMapper.updateDataTypeAnotherNameById(id, anotherName);
    }
}
