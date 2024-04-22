package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.DataType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataTypeMapper {

    int addDataType(DataType dataType);

    int removeDataTypeById(int id);

    int updateDataTypeAnotherNameById(@Param("id") int id, @Param("anotherName") String anotherName);

    DataType getDataTypeById(int id);

    List<DataType> getDataTypeListByUserId(int userId);

}
