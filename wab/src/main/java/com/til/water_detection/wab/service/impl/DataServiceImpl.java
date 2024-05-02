package com.til.water_detection.wab.service.impl;

import com.til.water_detection.data.Data;
import com.til.water_detection.data.DataType;
import com.til.water_detection.data.Equipment;
import com.til.water_detection.sql.mapper.DataMapper;
import com.til.water_detection.sql.mapper.DataTypeMapper;
import com.til.water_detection.sql.mapper.EquipmentMapper;
import com.til.water_detection.wab.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class DataServiceImpl implements IDataService {

    @Autowired
    private DataMapper dataMapper;

    @Override
    public int addData(Data data) {
        return dataMapper.addData(data);
    }

    @Override
    public Data getDataById(long id) {
        return dataMapper.getDataById(id);
    }

    @Override
    public List<Data> getAllData() {
        return dataMapper.getAllData();
    }

    @Override
    public List<Data> getData(int equipmentId, int dataTypeId, Timestamp start, Timestamp end) {
        return dataMapper.getData(equipmentId, dataTypeId, start, end);
    }

    @Override
    public    List<Data> getDataMapFromEquipmentIdArray(int[] equipmentIdArray, int dataTypeId, Timestamp start, Timestamp end) {
        return dataMapper.getDataMapFromEquipmentIdArray(equipmentIdArray, dataTypeId, start, end);
    }

    @Override
    public    List<Data> getDataMapFromDataTypeIdArray(int equipmentId, int[] dataTypeIdArray, Timestamp start, Timestamp end) {
        return dataMapper.getDataMapFromDataTypeIdArray(equipmentId, dataTypeIdArray, start, end);
    }
}
