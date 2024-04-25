package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.Detection;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DetectionMapper {

    @Insert("""
            insert into water_detection_data.detection ()
            values ()
            """)
    int addDetection();

    @Delete("""
            delete
            from water_detection_data.detection
            where id = #{id}
            """)
    int removeDetectionPosById(int id);

    @Update("""
            update water_detection_data.detection
            set another_name = #{anotherName}
            where id = #{id}
            """)
    int updateDetectionAnotherName(@Param("id") int id, @Param("anotherName") String anotherName);


    @Update("""
            update water_detection_data.detection
            set up_time = CURRENT_TIMESTAMP
            where id = #{id}
            """)
    int updateDetectionTimeById(int id);


    @Update("""
            update water_detection_data.detection
            set longitude = #{longitude},
                latitude  = #{latitude},
                up_time   = CURRENT_TIMESTAMP
            where id = #{id}
            """)
    int updateDetectionCoordinateById(@Param("id") int id, @Param("longitude") float longitude, @Param("latitude") float latitude);


    @Select("""
            select *
            from water_detection_data.detection
            where id = #{id}
            """)
    Detection getDetectionPosById(int id);


    @Select("""
            select *
            from water_detection_data.detection
            """)
    List<Detection> getAllDetection();
}
