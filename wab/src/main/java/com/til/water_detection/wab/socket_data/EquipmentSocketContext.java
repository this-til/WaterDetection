package com.til.water_detection.wab.socket_data;

import com.til.water_detection.data.Actuator;
import com.til.water_detection.data.DataType;
import com.til.water_detection.data.Equipment;
import com.til.water_detection.data.run_time.ActuatorRuntime;
import com.til.water_detection.data.run_time.DataTypeRunTime;
import com.til.water_detection.data.run_time.EquipmentRunTime;
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
    private boolean isInit;

    public EquipmentSocketContext(WebSocketSession webSocketSession) {
        super(webSocketSession);
    }

    public void setEquipment(Equipment equipment) {
        assert !isInit;
        this.equipmentRunTime = new EquipmentRunTime(equipment, null, null);
    }

    public void setActuatorRuntimeList(List<ActuatorRuntime> actuatorRuntimeList) {
        assert !isInit;
        this.equipmentRunTime.setActuatorRuntimeList(Collections.unmodifiableList(actuatorRuntimeList));
    }

    public void setDataTypeRuntimeList(List<DataTypeRunTime> dataTypeRuntimeList) {
        assert !isInit;
        this.equipmentRunTime.setDataTypeRuntimeList(Collections.unmodifiableList(dataTypeRuntimeList));
    }

    public boolean isInit() {
        return isInit;
    }

    public void initEnd() {
        assert !isInit;
        isInit = true;
    }

}
