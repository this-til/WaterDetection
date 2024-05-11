package com.til.water_detection.wab.service;

import com.til.water_detection.data.Rule;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IRuleService {

    int registerRule(Rule rule);

    int registerRuleSimple(int equipmentId, int dataTypeId);

    int deleteByID(int id);

    int updateById(int id, Rule rule);

    Rule getRuleById(int id);

    Rule getRuleByLimitId(int equipmentId, int dataTypeId);

    List<Rule> getRuleByEquipmentId(int equipmentId);

    List<Rule> getRuleByDataTypeId(int dataTypeId);

    List<Rule> getRuleByEquipmentIdArray(int[] id);

    List<Rule> getAllRule();
}
