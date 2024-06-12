package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.Equipment;
import org.apache.ibatis.annotations.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mapper
public interface IEquipmentMapper {

    @Insert("""
            insert into water_detection_data.equipment (name)
            values (#{name})
            """)
    int registerEquipment(@Param("name") String name);

    @Delete("""
            delete
            from water_detection_data.equipment
            where id = #{id}
            """)
    int removeEquipmentPosById(@Param("id") int id);

    @Update("""
            update water_detection_data.equipment
            set name = #{anotherName}
            where id = #{id}
            """)
    int updateEquipmentAnotherName(@Param("id") int id, @Param("anotherName") String anotherName);


    @Update("""
            update water_detection_data.equipment
            set up_time = CURRENT_TIMESTAMP
            where id = #{id}
            """)
    int updateEquipmentTimeById(@Param("id") int id);

    @Update("""
            update water_detection_data.equipment
            set latitude = #{latitude},longitude = #{longitude}
            where id = #{id}
            """)
    int updateEquipmentPosById(@Param("id") int id, @Param("longitude") float longitude, @Param("latitude") float latitude);

    @Update("""
            update water_detection_data.equipment
            set electronic_fence = #{electronicFence}, fence_latitude = #{latitude},fence_longitude = #{longitude}, fence_range = #{range}
            where id = #{id}
            """)
    int updateEquipmentFencePosById(@Param("id") int id, @Param("electronicFence") boolean electronicFence, @Param("longitude") float longitude, @Param("latitude") float latitude, @Param("range") float range);


    @Select("""
            select *
            from water_detection_data.equipment
            where id = #{id}
            """)
    @Nullable
    Equipment getEquipmentById(@Param("id") int id);

    @Select("""
            select *
            from water_detection_data.equipment
            where name = #{name}
            """)
    @Nullable
    Equipment getEquipmentByName(@Param("name") String name);

    @Select("""
            select *
            from water_detection_data.equipment
            """)
    List<Equipment> getAllEquipment();


    List<Equipment> getEquipmentByIdArray(@Param("id") int[] id);

    List<Equipment> getEquipmentByNameArray(@Param("name") String[] name);

}
