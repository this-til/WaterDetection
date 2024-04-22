package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.Data;
import com.til.water_detection.data.DataType;

import java.util.List;

public interface DataMapper {

    int addData(Data data);

    Data getDataById(Long id);

    List<Data> getDataListByUserId(int userId);

    List<Data> getDataListByDetectionPosId(int detectionPosId);

    List<Data> getDataListByDataTypeId(int dataTypeId);
}
