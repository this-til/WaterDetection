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

    @Update("""
            update water_detection_data.command
            set rule_id = #{command.ruleId},
                actuator_id = #{command.actuatorId},
                command_trigger = #{command.commandTrigger},
                upper = #{command.upper},
                low = #{command.low}
            where id = #{command.ruleId}
            """)
    int updateCommand(@Param("id") int ruleId,@Param("command") Command command);

    @Select("""
            select *
            from water_detection_data.command
            where id = #{id}
            """)
    Command getCommandById(@Param("id")  int id);

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
