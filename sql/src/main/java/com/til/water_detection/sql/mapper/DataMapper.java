package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.Data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface DataMapper {

    @Insert("""
            insert into water_detection_data.data ( detection_id, data_type_id, time, value)
            values ( #{detectionId}, #{dataTypeId}, #{time}, #{value})
            """)
    int addData(Data data);

    @Select("""
            select *
            from water_detection_data.data
            where id = #{id}
            """)
    Data getDataById(long id);

    @Select("""
            select *
            from water_detection_data.data
            """)
    List<Data> getAllData();

    @Select("""
            select *
            from water_detection_data.data
            where
                if(#{detectionPosId} < 0, TRUE , detection_id = #{detectionPosId})
                && if(#{dataTypeId} < 0 , TRUE , data_type_id = #{dataTypeId})
                && if(#{start} < 0, TRUE ,  time > #{start})
                && if(#{end} < 0, TRUE ,  time < #{end})
            """)
    List<Data> getData(
            @Param("detectionPosId") int detectionPosId,
            @Param("dataTypeId") int dataTypeId,
            @Param("start") long start,
            @Param("end") long end
    );
}
