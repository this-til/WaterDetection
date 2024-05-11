package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.Rule;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IRuleMapper {


    @Insert("""
            insert into water_detection_data.rule
            (
                equipment_id,
                data_type_id,
                exception_upper,
                warn_upper,
                warn_lower,
                exception_lower,
                exception_send_message,
                warn_send_message
            )
            values(
                #{equipmentId},
                #{dataTypeId},
                #{exceptionUpper},
                #{warnUpper},
                #{warnLower},
                #{exceptionLower},
                #{exceptionSendMessage},
                #{warnSendMessage}
            )
            """)
    int registerRule(Rule rule);

    @Insert("""
            insert into water_detection_data.rule(equipment_id, data_type_id)
            values (#{equipmentId}, #{dataTypeId})
            """)
    int registerRuleSimple(@Param("equipmentId") int equipmentId, @Param("dataTypeId")int dataTypeId);

    @Delete("""
            delete from water_detection_data.rule
            where id = #{id}
            """)
    int deleteByID(int id);

    @Update("""
            update water_detection_data.rule
            set
                exception_upper=#{rule.exceptionUpper},
                warn_upper=#{rule.warnUpper},
                warn_lower=#{rule.warnLower},
                exception_lower=#{rule.exceptionLower},
                exception_send_message=#{rule.exceptionSendMessage},
                warn_send_message=#{rule.warnSendMessage}
            where id = #{id}
            """)
    int updateById(@Param("id") int id,@Param("rule") Rule rule);

    @Select("""
            select *
            from water_detection_data.rule
            where id=#{id}
            """)
    Rule getRuleById(int id);

    @Select("""
            select *
            from water_detection_data.rule
            where equipment_id=#{equipmentId} and data_type_id=#{dataTypeId}
            """)
    Rule getRuleByLimitId(int equipmentId, int dataTypeId);

    @Select("""
            select *
            from water_detection_data.rule
            where equipment_id = #{equipmentId}
            """)
    List<Rule> getRuleByEquipmentId(int equipmentId);

    @Select("""
            select *
            from water_detection_data.rule
            where data_type_id = #{dataTypeId}
            """)
    List<Rule> getRuleByDataTypeId(int dataTypeId);

    List<Rule> getRuleByEquipmentIdArray(int[] id);

    @Select("""
            select *
            from water_detection_data.rule
            """)
    List<Rule> getAllRule();

}
