package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Equipment;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import com.til.water_detection.wab.service.IEquipmentService;
import com.til.water_detection.wab.util.FinalString;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
@Validated
@ResponseBody
public class EquipmentController {

    @Resource
    private IEquipmentService detectionService;

    /***
     * 添加用户自定义的数据 成功时需要重新拉取所有数据类型
     */
    @PostMapping("/register")
    public Result<Void> register() {
        detectionService.addEquipment();
        return Result.successful(null);
    }

    @DeleteMapping("/removeEquipmentPosById")
    public Result<Void> removeEquipmentPosById(@RequestParam int equipmentId) {
        int i = detectionService.removeEquipmentPosById(equipmentId);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, i + "条数据被删除", null);
    }

    @PostMapping("/getAllEquipment")
    public Result<List<Equipment>> getAllEquipment() {
        List<Equipment> equipmentList = detectionService.getAllEquipment();
        return new Result<>(ResultType.SUCCESSFUL, null, equipmentList);
    }

    @GetMapping("/getEquipmentById")
    public Result<Equipment> getEquipmentById(@RequestParam int id) {
        Equipment equipment = detectionService.getEquipmentById(id);
        return new Result<>(equipment != null ? ResultType.SUCCESSFUL : ResultType.FAIL, null, equipment);
    }

    @PostMapping("/getEquipmentByIdArray")
    public Result<List<Equipment>> getEquipmentByIdArray(@RequestBody int[] id) {
        return new Result<>(ResultType.SUCCESSFUL, null, detectionService.getEquipmentByIdArray(id));
    }

    @PutMapping("/updateEquipmentAnotherNameById")
    public Result<Void> updateEquipmentAnotherNameById(@RequestParam int id, @RequestParam @Param(FinalString.VERIFY_1_30) String anotherName) {
        int i = detectionService.updateEquipmentAnotherNameById(id, anotherName);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, i + "条数据被更改", null);
    }

    @PutMapping("/updateEquipmentTimeById")
    public Result<Void> updateEquipmentTimeById(@RequestParam int id) {
        int i = detectionService.updateEquipmentTimeById(id);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, i + "条数据被更改", null);
    }
}
