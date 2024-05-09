package com.til.water_detection.wab.socket_data;

import com.til.water_detection.data.Actuator;
import com.til.water_detection.data.DataType;
import com.til.water_detection.data.Equipment;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Getter
public class EquipmentSocketContext extends SocketContext<CommandCallback<EquipmentSocketContext>> {

    @Setter
    private Equipment equipment;
    private Map<Integer, DataType> dataTypeMap;
    private Map<Integer, Actuator> actuatorMap;

    public EquipmentSocketContext(WebSocketSession webSocketSession) {
        super(webSocketSession);
    }

    public EquipmentSocketContext setDataTypeMap(Map<Integer, DataType> dataTypeMap) {
        this.dataTypeMap = Collections.unmodifiableMap(dataTypeMap);
        return this;
    }

    public EquipmentSocketContext setActuatorMap(Map<Integer, Actuator> actuatorMap) {
        this.actuatorMap = Collections.unmodifiableMap(actuatorMap);
        return this;
    }

}
