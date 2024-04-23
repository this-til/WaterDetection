package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.Data;
import com.til.water_detection.data.DataType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DataMapper {

    @Insert("""
            insert into water_detection_data.data (user_id, detection_id, data_type_id, time, value)
            values (#{userId}, #{detectionId}, #{dataTypeId}, #{time}, #{value})
            """)
    int addData(Data data);

    @Select("""
            select *
            from water_detection_data.data
            where id = #{id}
            """)
    Data getDataById(Long id);

    @Select("""
            select *
            from water_detection_data.data
            where user_id = #{userId}
            """)
    List<Data> getDataListByUserId(int userId);

    @Select("""
            select *
            from water_detection_data.data
            where detection_id = #{detectionPosId}
            """)
    List<Data> getDataListByDetectionPosId(int detectionPosId);

    @Select("""
            select *
            from water_detection_data.data
            where data_type_id = #{dataTypeId}
            """)
    List<Data> getDataListByDataTypeId(int dataTypeId);
}
