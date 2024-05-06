package com.til.water_detection.wab.service;

import com.til.water_detection.data.Rule;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface IRuleService {

    int registerRule(Rule rule);

    int deleteByID(int id);

    int updateById(int id, Rule rule);

    Rule getRuleById(int id);

    List<Rule> getRuleByEquipmentId(int equipmentId);

    List<Rule> getRuleByDataTypeId(int dataTypeId);

    List<Rule> getRuleByEquipmentIdArray(int[] id);

    List<Rule> getAllRule();
}
