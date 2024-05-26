package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Equipment;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.state.ResultType;
import com.til.water_detection.data.run_time.EquipmentRunTime;
import com.til.water_detection.data.util.FinalByte;
import com.til.water_detection.wab.service.IEquipmentService;
import com.til.water_detection.data.util.FinalString;
import com.til.water_detection.wab.socket_data.CommandCallback;
import com.til.water_detection.wab.socket_data.EquipmentSocketContext;
import com.til.water_detection.wab.socket_handler.EquipmentSocketHandler;
import com.til.water_detection.wab.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/equipment")
@Validated
@ResponseBody
public class EquipmentController {

    @Resource
    private IEquipmentService equipmentService;

    @Resource
    private EquipmentSocketHandler equipmentSocketHandler;

    /***
     * 添加用户自定义的数据 成功时需要重新拉取所有数据类型
     */
    @PostMapping("/registerEquipment")
    public Result<Void> registerEquipment(@RequestParam @Param(FinalString.VERIFY_1_32) String name) {
        int i = equipmentService.registerEquipment(name);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, null, null);
    }

    @DeleteMapping("/removeEquipmentPosById")
    public Result<Void> removeEquipmentPosById(@RequestParam int equipmentId) {
        int i = equipmentService.removeEquipmentPosById(equipmentId);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, null, null);
    }

    @PutMapping("/updateEquipmentAnotherNameById")
    public Result<Void> updateEquipmentAnotherNameById(@RequestParam int id, @RequestParam @Param(FinalString.VERIFY_1_32) String anotherName) {

        Equipment equipment = equipmentService.getEquipmentByName(anotherName);
        if (equipment != null) {
            return new Result<>(ResultType.FAIL, "名称重复", null);
        }

        int i = equipmentService.updateEquipmentAnotherNameById(id, anotherName);

        Optional<EquipmentSocketContext> first = equipmentSocketHandler.getSocketContext()
                .stream()
                .filter(e -> e.getEquipmentRunTime().getEquipment().getId() == id)
                .findFirst();

        if (first.isPresent()) {
            EquipmentSocketContext equipmentSocketContext = first.get();
            ByteBuf byteBuf = Unpooled.buffer()
                    .writeByte(FinalByte.WRITE)
                    .writeByte(FinalByte.EQUIPMENT);
            ByteBufUtil.writeString(byteBuf, anotherName);
            equipmentSocketContext.addCommandCallback(new CommandCallback<>(byteBuf));
        }

        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, null, null);
    }

    @PutMapping("/updateEquipmentTimeById")
    public Result<Void> updateEquipmentTimeById(@RequestParam int id) {
        int i = equipmentService.updateEquipmentTimeById(id);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, null, null);
    }

    @PutMapping("/updateEquipmentPosById")
    public Result<Void> updateEquipmentPosById(@RequestParam int id, @RequestParam float longitude, @RequestParam float latitude) {
        int i = equipmentService.updateEquipmentPosById(id, longitude, latitude);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, null, null);
    }

    @PutMapping("/updateEquipmentFencePosById")
    public Result<Void> updateEquipmentFencePosById(@RequestParam int id, @RequestParam boolean electronicFence, @RequestParam float longitude, @RequestParam float latitude) {
        int i = equipmentService.updateEquipmentFencePosById(id, electronicFence, longitude, latitude);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, null, null);
    }

    @GetMapping("/getEquipmentById")
    public Result<Equipment> getEquipmentById(@RequestParam int id) {
        Equipment equipment = equipmentService.getEquipmentById(id);
        return new Result<>(equipment != null ? ResultType.SUCCESSFUL : ResultType.FAIL, null, equipment);
    }

    @GetMapping("/getEquipmentByName")
    public Result<Equipment> getEquipmentByName(@RequestParam String name) {
        Equipment equipment = equipmentService.getEquipmentByName(name);
        return new Result<>(equipment != null ? ResultType.SUCCESSFUL : ResultType.FAIL, null, equipment);
    }

    @GetMapping("/getAllEquipment")
    public Result<List<Equipment>> getAllEquipment() {
        List<Equipment> equipmentList = equipmentService.getAllEquipment();
        return new Result<>(ResultType.SUCCESSFUL, null, equipmentList);
    }

    @GetMapping("/getAllOnlineEquipment")
    public Result<List<Equipment>> getAllOnlineEquipment() {
        return new Result<>(ResultType.SUCCESSFUL, null,
                equipmentSocketHandler.getSocketContext()
                        .stream()
                        .map(EquipmentSocketContext::getEquipmentRunTime)
                        .map(EquipmentRunTime::getEquipment)
                        .toList());
    }

    @GetMapping("/getAllOnlineEquipmentId")
    public Result<List<Integer>> getAllOnlineEquipmentId() {
        return new Result<>(ResultType.SUCCESSFUL, null,
                equipmentSocketHandler.getSocketContext()
                        .stream()
                        .map(EquipmentSocketContext::getEquipmentRunTime)
                        .map(EquipmentRunTime::getEquipment)
                        .map(Equipment::getId)
                        .toList());
    }

    @GetMapping("/getOnlineEquipment")
    public Result<EquipmentRunTime> getOnlineEquipment(@RequestParam int id) {
        return equipmentSocketHandler.getSocketContext()
                .stream()
                .filter(e -> e.getEquipmentRunTime().getEquipment().getId() == id)
                .findFirst()
                .map(equipmentSocketContext -> new Result<>(ResultType.SUCCESSFUL, null, equipmentSocketContext.getEquipmentRunTime()))
                .orElseGet(() -> new Result<>(ResultType.SUCCESSFUL, null, null));
    }

    @GetMapping("/getEquipmentByIdArray")
    public Result<List<Equipment>> getEquipmentByIdArray(@RequestParam int[] id) {
        return new Result<>(ResultType.SUCCESSFUL, null, equipmentService.getEquipmentByIdArray(id));
    }

    @GetMapping("/getEquipmentByNameArray")
    public Result<List<Equipment>> getEquipmentByNameArray(@RequestParam String[] name) {
        return new Result<>(ResultType.SUCCESSFUL, null, equipmentService.getEquipmentByNameArray(name));
    }
}
