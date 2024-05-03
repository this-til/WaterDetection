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
    int removeEquipmentPosById(@Param("id") int id);

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
    int updateEquipmentTimeById(@Param("id") int id);

    @Select("""
            select *
            from water_detection_data.equipment
            where id = #{id}
            """)
    Equipment getEquipmentById(@Param("id") int id);

    List<Equipment> getEquipmentByIdArray(@Param("id") int[] id);


    @Select("""
            select *
            from water_detection_data.equipment
            """)
    List<Equipment> getAllEquipment();

}
