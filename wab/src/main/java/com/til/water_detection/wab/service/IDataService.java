package com.til.water_detection.wab.service;

import com.til.water_detection.data.Data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface IDataService {
    int addData(Data data);

    Data getDataById(long id);

    List<Data> getAllData();

    List<Data> getData(
            int equipmentId,
            int dataTypeId,
            Timestamp start,
            Timestamp end
    );

    List<Data> getDataMapFromEquipmentIdArray(int[] equipmentIdArray, int dataTypeId, Timestamp start, Timestamp end);

    List<Data> getDataMapFromDataTypeIdArray(int equipmentId, int[] dataTypeIdArray, Timestamp start, Timestamp end);
}
