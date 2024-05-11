package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Equipment;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import com.til.water_detection.wab.service.IEquipmentService;
import com.til.water_detection.data.util.FinalString;
import com.til.water_detection.wab.socket_data.EquipmentSocketContext;
import com.til.water_detection.wab.socket_handler.EquipmentSocketHandler;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/equipment")
@Validated
@ResponseBody
public class EquipmentController {

    @Resource
    private IEquipmentService detectionService;

    @Resource
    private EquipmentSocketHandler equipmentSocketHandler;

    /***
     * 添加用户自定义的数据 成功时需要重新拉取所有数据类型
     */
    @PostMapping("/registerEquipment")
    public Result<Void> registerEquipment(@RequestParam @Param(FinalString.VERIFY_1_32) String name) {
        int i = detectionService.registerEquipment(name);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, i + "条数据被添加", null);
    }

    @DeleteMapping("/removeEquipmentPosById")
    public Result<Void> removeEquipmentPosById(@RequestParam int equipmentId) {
        int i = detectionService.removeEquipmentPosById(equipmentId);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, i + "条数据被删除", null);
    }

    @PutMapping("/updateEquipmentAnotherNameById")
    public Result<Void> updateEquipmentAnotherNameById(@RequestParam int id, @RequestParam @Param(FinalString.VERIFY_1_32) String anotherName) {
        int i = detectionService.updateEquipmentAnotherNameById(id, anotherName);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, i + "条数据被更改", null);
    }

    @PutMapping("/updateEquipmentTimeById")
    public Result<Void> updateEquipmentTimeById(@RequestParam int id) {
        int i = detectionService.updateEquipmentTimeById(id);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, i + "条数据被更改", null);
    }

    @GetMapping("/getEquipmentById")
    public Result<Equipment> getEquipmentById(@RequestParam int id) {
        Equipment equipment = detectionService.getEquipmentById(id);
        return new Result<>(equipment != null ? ResultType.SUCCESSFUL : ResultType.FAIL, null, equipment);
    }

    @GetMapping("/getEquipmentByName")
    public Result<Equipment> getEquipmentByName(@RequestParam String name) {
        Equipment equipment = detectionService.getEquipmentByName(name);
        return new Result<>(equipment != null ? ResultType.SUCCESSFUL : ResultType.FAIL, null, equipment);
    }

    @GetMapping("/getAllEquipment")
    public Result<List<Equipment>> getAllEquipment() {
        List<Equipment> equipmentList = detectionService.getAllEquipment();
        return new Result<>(ResultType.SUCCESSFUL, null, equipmentList);
    }

    @GetMapping("/getAllOnlineEquipment")
    public Result<List<Equipment>> getAllOnlineEquipment() {
        return new Result<>(ResultType.SUCCESSFUL, null,
                equipmentSocketHandler.getSocketContext()
                        .stream()
                        .map(EquipmentSocketContext::getEquipment)
                        .toList());

    }

    @GetMapping("/getEquipmentByIdArray")
    public Result<List<Equipment>> getEquipmentByIdArray(@RequestParam int[] id) {
        return new Result<>(ResultType.SUCCESSFUL, null, detectionService.getEquipmentByIdArray(id));
    }

    @GetMapping("/getEquipmentByNameArray")
    public Result<List<Equipment>> getEquipmentByNameArray(@RequestParam String[] name) {
        return new Result<>(ResultType.SUCCESSFUL, null, detectionService.getEquipmentByNameArray(name));
    }
}
