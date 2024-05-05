package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.Actuator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IActuatorMapper {


    @Insert("""
            insert into  water_detection_data.actuator(name)
            values (#{name})
            """)
    int registerActuator(String name);

    @Delete("""
            delete
            from water_detection_data.actuator
            where id = #{id}
            """)
    int removeActuatorById(int actuatorId);

    @Select("""
            select *
            from water_detection_data.actuator
            where id = #{id}
            """)
    Actuator getActuatorById(int id);

    @Select("""
            select *
            from water_detection_data.actuator
            where name = #{name}
            """)
    Actuator getActuatorByName(String name);

    List<Actuator> getActuatorByIdArray(int[] id);

    List<Actuator> getActuatorByNameArray(String[] name);

    @Select("""
            select *
            from water_detection_data.actuator
            """)
    List<Actuator> getAllActuator();

}
