package com.til.water_detection.wab.service.impl;

import com.til.water_detection.data.Rule;
import com.til.water_detection.sql.mapper.IRuleMapper;
import com.til.water_detection.wab.service.IRuleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleServiceImpl implements IRuleService {

    @Resource
    private IRuleMapper ruleMapper;

    @Override
    public int registerRule(Rule rule) {
        return ruleMapper.registerRule(rule);
    }

    @Override
    public int registerRuleSimple(int equipmentId, int dataTypeId) {
        return ruleMapper.registerRuleSimple(equipmentId, dataTypeId);
    }

    @Override
    public int deleteByID(int id) {
        return ruleMapper.deleteByID(id);
    }

    @Override
    public int updateById(int id, Rule rule) {
        return ruleMapper.updateById(id, rule);
    }

    @Override
    public Rule getRuleById(int id) {
        return ruleMapper.getRuleById(id);
    }

    @Override
    public Rule getRuleByLimitId(int equipmentId, int dataTypeId) {
        return ruleMapper.getRuleByLimitId(equipmentId, dataTypeId);
    }

    @Override
    public List<Rule> getRuleByEquipmentId(int equipmentId) {
        return ruleMapper.getRuleByEquipmentId(equipmentId);
    }

    @Override
    public List<Rule> getRuleByDataTypeId(int dataTypeId) {
        return ruleMapper.getRuleByDataTypeId(dataTypeId);
    }

    @Override
    public List<Rule> getRuleByEquipmentIdArray(int[] id) {
        return ruleMapper.getRuleByEquipmentIdArray(id);
    }

    @Override
    public List<Rule> getAllRule() {
        return ruleMapper.getAllRule();
    }
}
