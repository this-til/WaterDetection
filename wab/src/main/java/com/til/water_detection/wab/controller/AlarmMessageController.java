package com.til.water_detection.wab.controller;

import com.til.water_detection.data.AlarmMessage;
import com.til.water_detection.data.Equipment;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.run_time.DataTypeRunTime;
import com.til.water_detection.data.state.DataState;
import com.til.water_detection.data.state.ResultType;
import com.til.water_detection.wab.service.IEquipmentService;
import com.til.water_detection.wab.socket_data.EquipmentSocketContext;
import com.til.water_detection.wab.socket_handler.CommandSocketHandlerBasics;
import com.til.water_detection.wab.socket_handler.EquipmentSocketHandler;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/alarmMessage")
@Validated
@ResponseBody
public class AlarmMessageController {

    @Resource
    private EquipmentSocketHandler equipmentSocketHandler;

    @Resource
    private IEquipmentService equipmentService;


    @PostMapping("/getAlarmMessageList")
    public Result<List<AlarmMessage>> getAlarmMessageList() {
        List<AlarmMessage> alarmMessageList = new ArrayList<>();
        Collection<EquipmentSocketContext> socketContext = equipmentSocketHandler.getSocketContext();
        HashSet<Integer> onlineDeviceID = new HashSet<>();

        for (EquipmentSocketContext equipmentSocketContext : socketContext) {

            for (DataTypeRunTime dataTypeRunTime : equipmentSocketContext.getEquipmentRunTime().getDataTypeRuntimeList()) {

                if (dataTypeRunTime.getDataState() == DataState.NORMAL) {
                    continue;
                }

                String state = "";
                switch (dataTypeRunTime.getDataState()) {
                    case EXCEPTION_UPPER -> state = "过高异常";
                    case WARN_UPPER -> state = "过高警告";
                    case WARN_LOWER -> state = "过低警告";
                    case EXCEPTION_LOWER -> state = "过低异常";
                }
                alarmMessageList.add(new AlarmMessage(
                        (long) equipmentSocketContext.getEquipmentRunTime().getEquipment().getId() << 32 | dataTypeRunTime.getDataType().getId(),
                        equipmentSocketContext.getEquipmentRunTime().getEquipment().getName() + ":" + dataTypeRunTime.getDataType().getName() + " " + state,
                        dataTypeRunTime.getDataState()
                ));

            }

            onlineDeviceID.add(equipmentSocketContext.getEquipmentRunTime().getEquipment().getId());
        }

        for (Equipment equipment : equipmentService.getAllEquipment()) {
            if (onlineDeviceID.contains(equipment.getId())) {
                continue;
            }
            alarmMessageList.add(new AlarmMessage(
                    (long) equipment.getId() << 32,
                    equipment.getName() + " 异常离线",
                    DataState.EXCEPTION_LOWER
            ));
        }


        return new Result<>(ResultType.SUCCESSFUL, "", alarmMessageList);
    }
}
