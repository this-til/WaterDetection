package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.Actuator;
import org.apache.ibatis.annotations.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mapper
public interface IActuatorMapper {

    @Insert("""
            insert into  water_detection_data.actuator(name)
            values (#{name})
            """)
    int registerActuator(@Param("name") String name);

    @Delete("""
            delete
            from water_detection_data.actuator
            where id = #{id}
            """)
    int removeActuatorById(@Param("actuatorId") int actuatorId);

    @Select("""
            select *
            from water_detection_data.actuator
            where id = #{id}
            """)
    @Nullable
    Actuator getActuatorById(@Param("id") int id);

    @Select("""
            select *
            from water_detection_data.actuator
            where name = #{name}
            """)
    @Nullable
    Actuator getActuatorByName(@Param("name") String name);

    List<Actuator> getActuatorByIdArray(@Param("id") int[] id);

    List<Actuator> getActuatorByNameArray(@Param("name") String[] name);

    @Select("""
            select *
            from water_detection_data.actuator
            """)
    List<Actuator> getAllActuator();

}
