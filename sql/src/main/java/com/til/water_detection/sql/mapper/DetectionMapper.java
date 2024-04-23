package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.Detection;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DetectionMapper {

    @Insert("""
            insert into water_detection_data.detection (user_id)
            values (#{userId}})
            """)
    int addDetection(@Param("userId") int userId);

    @Delete("""
            delete
            from water_detection_data.detection
            where id = #{id}
            """)
    int removeDetectionPosById(int id);


    @Delete("""
            delete
            from water_detection_data.detection
            where id = #{id} && user_id = #{userId}
            """)
    int removeDetectionPosById(@Param("id") int id,@Param("userid") int userId);

    @Update("""
            update water_detection_data.detection
            set another_name = #{anotherName}
            where id = #{id}
            """)
    int updateDetectionAnotherNameById(@Param("id") int id, @Param("anotherName") String anotherName);

    @Update("""
            update water_detection_data.detection
            set another_name = #{anotherName}
            where id = #{id} && user_id = #{userId}
            """)
    int updateDetectionAnotherNameById(@Param("id") int id, @Param("userid") int userId, @Param("anotherName") String anotherName);

    @Update("""
            update water_detection_data.detection
            set up_time = CURRENT_TIMESTAMP
            where id = #{id}
            """)
    int updateDetectionTimeById(int id);

    @Update("""
            update water_detection_data.detection
            set up_time = CURRENT_TIMESTAMP
            where id = #{id} && user_id = #{userId}
            """)
    int updateDetectionTimeById(@Param("id")int id, @Param("userId") int userId);

    @Update("""
            update water_detection_data.detection
            set longitude = #{longitude},
                latitude  = #{latitude},
                up_time   = CURRENT_TIMESTAMP
            where id = #{id}
            """)
    int updateDetectionCoordinateById(@Param("id") int id, @Param("longitude") float longitude, @Param("latitude") float latitude);

    @Update("""
            update water_detection_data.detection
            set longitude = #{longitude},
                latitude  = #{latitude},
                up_time   = CURRENT_TIMESTAMP
            where id = #{id} && user_id = #{userId}
            """)
    int updateDetectionCoordinateById(@Param("id") int id, @Param("userId") int userId, @Param("longitude") float longitude, @Param("latitude") float latitude);


    @Select("""
            select *
            from water_detection_data.detection
            where id = #{id}
            """)
    Detection getDetectionPosById(int id);

    @Select("""
            select *
            from water_detection_data.detection
            where id = #{id} && user_id = #{userId}
            """)
    Detection getDetectionPosById(int id, int userId);

    @Select("""
            select *
            from water_detection_data.detection
            where user_id = #{userId}
            """)
    List<Detection> getDetectionListByUserId(int userId);
}
