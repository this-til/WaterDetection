package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.Command;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ICommandMapper {


    @Insert("""
            insert into water_detection_data.command( rule_id, actuator_id, command_trigger)
            values (#{ruleId}, #{actuatorId}, #{commandTrigger})
            """)
    int registerCommand(@Param("ruleId") int ruleId, @Param("actuatorId") int actuatorId, @Param("commandTrigger") int commandTrigger);

    @Delete("""
            delete from water_detection_data.command
            where id = #{id}
            """)
    int removeCommandById(@Param("id") int id);

    @Select("""
            select * 
            from water_detection_data.command
            where id = #{id}
            """)
    Command getCommandById(int id);

    List<Command> getCommandByIdArray(@Param("id") int[] id);

    @Select("""
            select * from water_detection_data.command
            where actuator_id = #{actuatorId}
            """)
    List<Command> getCommandByActuatorId(@Param("actuatorId") int actuatorId);

    @Select("""
            select *
            from water_detection_data.command
            where rule_id = #{ruleId}
            """)
    List<Command> getCommandByRuleId(@Param("ruleId") int ruleId);

    @Select("""
            select *
            from water_detection_data.command
            """)
    List<Command> getAllCommands();


}
