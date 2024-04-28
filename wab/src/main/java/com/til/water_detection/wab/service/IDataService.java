package com.til.water_detection.wab.service;

import com.til.water_detection.data.Data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.util.List;

public interface IDataService {
    int addData(Data data);

    Data getDataById(long id);

    List<Data> getAllData();

    List<Data> getData(
            int equipmentId,
            int dataTypeId,
            long start,
            long end
    );
}
