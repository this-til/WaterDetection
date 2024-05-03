package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.Data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Mapper
public interface DataMapper {

    @Insert("""
            insert into water_detection_data.data ( equipment_id, data_type_id, time, value)
            values ( #{equipmentId}, #{dataTypeId}, #{time}, #{value})
            """)
    int addData(Data data);

    @Select("""
            select *
            from water_detection_data.data
            where id = #{id}
            """)
    Data getDataById(@Param("id") long id);

    @Select("""
            select *
            from water_detection_data.data
            """)
    List<Data> getAllData();

    @Select("""
            select *
            from water_detection_data.data
            where
                if(#{equipmentId} < 0, TRUE , equipment_id = #{equipmentId})
                && if(#{dataTypeId} < 0 , TRUE , data_type_id = #{dataTypeId})
                && if(#{start} < 0, TRUE ,  time > #{start})
                && if(#{end} < 0, TRUE ,  time < #{end})
            """)
    List<Data> getData(
            @Param("equipmentId") int equipmentId,
            @Param("dataTypeId") int dataTypeId,
            @Param("start") Timestamp start,
            @Param("end") Timestamp end
    );

    List<Data> getDataMapFromEquipmentIdArray(
           @Param("equipmentIdArray") int[] equipmentIdArray,
           @Param("dataTypeId") int dataTypeId,
           @Param("start") Timestamp start,
           @Param("end") Timestamp end);

    List<Data> getDataMapFromDataTypeIdArray(
          @Param("equipmentId") int equipmentId,
          @Param("dataTypeIdArray") int[] dataTypeIdArray,
          @Param("start") Timestamp start,
          @Param("end") Timestamp end);
}
