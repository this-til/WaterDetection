package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Actuator;
import com.til.water_detection.data.Equipment;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.run_time.ActuatorRuntime;
import com.til.water_detection.data.run_time.EquipmentRunTime;
import com.til.water_detection.data.state.ResultType;
import com.til.water_detection.wab.service.IActuatorService;
import com.til.water_detection.data.util.FinalString;
import com.til.water_detection.wab.service.IEquipmentService;
import com.til.water_detection.wab.socket_data.CommandCallback;
import com.til.water_detection.wab.socket_data.EquipmentSocketContext;
import com.til.water_detection.wab.socket_handler.EquipmentSocketHandler;
import io.netty.buffer.Unpooled;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actuator")
@Validated
@ResponseBody
public class ActuatorController {
    @Resource
    private IActuatorService actuatorService;

    @Resource
    private IEquipmentService equipmentService;

    @Resource
    private EquipmentSocketHandler equipmentSocketHandler;

    @PostMapping("/registerActuator")
    public Result<Void> registerActuator(@RequestParam @Param(FinalString.VERIFY_1_32) String name) {
        int i = actuatorService.registerActuator(name);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @DeleteMapping("/removeActuatorById")
    public Result<Void> removeActuatorById(@RequestParam int id) {
        int i = actuatorService.removeActuatorById(id);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @PutMapping("/updateActuatorByEquipmentId")
    public Result<Void> updateActuatorByEquipmentId(@RequestParam int equipmentId, @RequestParam int embeddedId, @RequestParam boolean open) {
        EquipmentSocketContext equipmentSocketContextByid = equipmentSocketHandler.getEquipmentSocketContextByid(equipmentId);
        if (equipmentSocketContextByid == null) {
            return new Result<>(ResultType.FAIL, "equipmentSocketContextByid is null", null);
        }
        List<ActuatorRuntime> actuatorRuntimeList = equipmentSocketContextByid.getEquipmentRunTime().getActuatorRuntimeList();
        if (embeddedId < 0 || embeddedId > actuatorRuntimeList.size()) {
            return new Result<>(ResultType.FAIL, "embeddedId is out of range", null);
        }
        actuatorRuntimeList.get(embeddedId).setActivated(open);
        equipmentSocketContextByid.addCommandCallback(new CommandCallback<>(
                Unpooled.buffer()
                        .writeByte(0x06)
                        .writeByte(0x01)
                        .writeInt(embeddedId)
                        .writeBoolean(open)
        ));
        return new Result<>(ResultType.SUCCESSFUL, null, null);
    }

    @GetMapping("/getActuatorById")
    public Result<Actuator> getActuatorById(@RequestParam int id) {
        Actuator actuatorById = actuatorService.getActuatorById(id);
        return new Result<>(actuatorById == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @GetMapping("/getActuatorByName")
    public Result<Actuator> getActuatorByName(@RequestParam String name) {
        Actuator actuatorByName = actuatorService.getActuatorByName(name);
        return new Result<>(actuatorByName == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @GetMapping("/getAllActuator")
    public Result<List<Actuator>> getAllActuator() {
        return new Result<>(ResultType.SUCCESSFUL, null, actuatorService.getAllActuator());
    }

    @GetMapping("/getActuatorByIdArray")

    public Result<List<Actuator>> getActuatorByIdArray(@RequestParam int[] id) {
        return new Result<>(ResultType.SUCCESSFUL, null, actuatorService.getActuatorByIdArray(id));
    }

    @GetMapping("/getActuatorByNameArray")
    public Result<List<Actuator>> getActuatorByNameArray(@RequestParam String[] name) {
        return new Result<>(ResultType.SUCCESSFUL, null, actuatorService.getActuatorByNameArray(name));
    }
}
