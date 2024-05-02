package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.Equipment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EquipmentMapper {

    @Insert("""
            insert into water_detection_data.equipment ()
            values ()
            """)
    int addEquipment();

    @Delete("""
            delete
            from water_detection_data.equipment
            where id = #{id}
            """)
    int removeEquipmentPosById(int id);

    @Update("""
            update water_detection_data.equipment
            set another_name = #{anotherName}
            where id = #{id}
            """)
    int updateEquipmentAnotherName(@Param("id") int id, @Param("anotherName") String anotherName);


    @Update("""
            update water_detection_data.equipment
            set up_time = CURRENT_TIMESTAMP
            where id = #{id}
            """)
    int updateEquipmentTimeById(int id);


    @Update("""
            update water_detection_data.equipment
            set longitude = #{longitude},
                latitude  = #{latitude},
                up_time   = CURRENT_TIMESTAMP
            where id = #{id}
            """)
    int updateEquipmentCoordinateById(@Param("id") int id, @Param("longitude") float longitude, @Param("latitude") float latitude);


    @Select("""
            select *
            from water_detection_data.equipment
            where id = #{id}
            """)
    Equipment getEquipmentById(int id);

    @Select("""
            select *
            from water_detection_data.data
            where id IN #{id}
            """)
    List<Equipment> getEquipmentByIdArray(int[] id);


    @Select("""
            select *
            from water_detection_data.equipment
            """)
    List<Equipment> getAllEquipment();

}
