package com.til.water_detection.wab.socket_data;

import com.til.water_detection.data.Actuator;
import com.til.water_detection.data.DataType;
import com.til.water_detection.data.Equipment;
import com.til.water_detection.data.run_time.ActuatorRuntime;
import com.til.water_detection.data.run_time.DataTypeRunTime;
import com.til.water_detection.data.run_time.EquipmentRunTime;
import com.til.water_detection.wab.socket_handler.EquipmentSocketHandler;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Getter
public class EquipmentSocketContext extends SocketContext<CommandCallback<EquipmentSocketContext>> {

    @Getter
    private EquipmentRunTime equipmentRunTime;
    private final EquipmentSocketHandler equipmentSocketHandler;

    public EquipmentSocketContext(WebSocketSession webSocketSession, EquipmentSocketHandler equipmentSocketHandler) {
        super(webSocketSession);
        this.equipmentSocketHandler = equipmentSocketHandler;
    }

    public void setEquipment(Equipment equipment) {
        this.equipmentRunTime = new EquipmentRunTime(equipment, null, null);
    }

    public void setActuatorRuntimeList(List<ActuatorRuntime> actuatorRuntimeList) {
        this.equipmentRunTime.setActuatorRuntimeList(Collections.unmodifiableList(actuatorRuntimeList));
    }

    public void setDataTypeRuntimeList(List<DataTypeRunTime> dataTypeRuntimeList) {
        this.equipmentRunTime.setDataTypeRuntimeList(Collections.unmodifiableList(dataTypeRuntimeList));
    }


    public EquipmentRunTime upEquipmentRunTime() {
        equipmentRunTime.setEquipment(equipmentSocketHandler.getEquipmentService().getEquipmentById(equipmentRunTime.getEquipment().getId()));
        for (DataTypeRunTime dataTypeRunTime : equipmentRunTime.getDataTypeRuntimeList()) {
            dataTypeRunTime.setRule(equipmentSocketHandler.getRuleService().getRuleById(dataTypeRunTime.getRule().getId()));
            dataTypeRunTime.setDataState(dataTypeRunTime.getRule().ofDataState(dataTypeRunTime.getValue()));
        }
        return equipmentRunTime;
    }

}
