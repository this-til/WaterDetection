package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.DataType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DataTypeMapper {

    @Insert("""
            insert into water_detection_data.data_type ()
            values ()
            """)
    int addDataType();

    @Delete("""
            delete
            from water_detection_data.data_type
            where id = #{id}
            """)
    int removeDataTypeById(int id);

    @Update("""
            update water_detection_data.data_type
            set another_name = #{anotherName}
            where id = #{id}
            """)
    int updateDataTypeAnotherNameById(@Param("id") int id, @Param("anotherName") String anotherName);

    @Select("""
            select *
            from water_detection_data.data_type
            where id = #{id}
            """)
    DataType getDataTypeById(int id);

    @Select("""
            select *
            from water_detection_data.data_type
            """)
    List<DataType> getAllDataType();

}
