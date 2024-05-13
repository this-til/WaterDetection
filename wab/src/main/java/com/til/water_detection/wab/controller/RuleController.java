package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Result;
import com.til.water_detection.data.state.ResultType;
import com.til.water_detection.data.Rule;
import com.til.water_detection.wab.service.IRuleService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rule")
@Validated
@ResponseBody
public class RuleController {

    @Resource
    private IRuleService ruleService;


    @PostMapping("/registerRule")
    public Result<Void> registerRule(@RequestParam Rule rule) {
        int i = ruleService.registerRule(rule);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @DeleteMapping("/deleteByID")
    public Result<Void> deleteByID(@RequestParam int id) {
        int i = ruleService.deleteByID(id);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @PutMapping("/updateById")
    public Result<Void> updateById(@RequestParam int id, @RequestBody Rule rule) {
        int i = ruleService.updateById(id, rule);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @GetMapping("/getRuleById")
    public Result<Rule> getRuleById(@RequestParam int id) {
        Rule rule = ruleService.getRuleById(id);
        return new Result<>(rule == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, rule);
    }

    @GetMapping("/getRuleByEquipmentId")
    public Result<List<Rule>> getRuleByEquipmentId(@RequestParam int equipmentId) {
        List<Rule> rules = ruleService.getRuleByEquipmentId(equipmentId);
        return new Result<>(rules == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, rules);
    }

    @GetMapping("/getRuleByDataTypeId")
    public Result<List<Rule>> getRuleByDataTypeId(@RequestParam int dataTypeId) {
        List<Rule> rules = ruleService.getRuleByDataTypeId(dataTypeId);
        return new Result<>(rules == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, rules);
    }

    @GetMapping("/getRuleByEquipmentIdArray")
    public Result<List<Rule>> getRuleByEquipmentIdArray(@RequestParam int[] id) {
        List<Rule> rules = ruleService.getRuleByEquipmentIdArray(id);
        return new Result<>(rules == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, rules);
    }

    @GetMapping("/getAllRule")
    public Result<List<Rule>> getAllRule() {
        List<Rule> rules = ruleService.getAllRule();
        return new Result<>(rules == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, rules);
    }

}
