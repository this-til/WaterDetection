package com.til.water_detection.wab.socket_data;

import com.til.water_detection.data.Equipment;
import com.til.water_detection.data.run_time.ActuatorRuntime;
import com.til.water_detection.data.run_time.DataTypeRunTime;
import com.til.water_detection.data.run_time.EquipmentRunTime;
import com.til.water_detection.data.state.DataState;
import com.til.water_detection.script.IActuator;
import com.til.water_detection.script.IDataType;
import com.til.water_detection.script.IEquipment;
import com.til.water_detection.wab.socket_handler.EquipmentSocketHandler;
import groovy.lang.Script;
import io.netty.buffer.Unpooled;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Getter
public class EquipmentSocketContext extends SocketContext<CommandCallback<EquipmentSocketContext>> implements IEquipment {

    protected static final Logger logger = LogManager.getLogger(EquipmentSocketContext.class);


    @Getter
    private EquipmentRunTime equipmentRunTime;
    private final EquipmentSocketHandler equipmentSocketHandler;

    private final Map<String, IActuator> actuatorMap = new HashMap<>();
    private final Map<String, IDataType> dataTypeMap = new HashMap<>();


    @Getter
    @Setter
    private boolean haveUpdate;

    @Getter
    @Setter
    private Script script;

    public EquipmentSocketContext(WebSocketSession webSocketSession, EquipmentSocketHandler equipmentSocketHandler) {
        super(webSocketSession);
        this.equipmentSocketHandler = equipmentSocketHandler;
    }

    public void setEquipment(Equipment equipment) {
        this.equipmentRunTime = new EquipmentRunTime(equipment, null, null, "");
        upScript();
    }

    public void upScript() {
        try {
            script = equipmentSocketHandler.getGroovyShell().parse(equipmentSocketHandler.getScriptService().getScriptById(equipmentRunTime.getEquipment().getId()));
        } catch (CompilationFailedException e) {
            logger.error(e);
        }
    }

    public void setActuatorRuntimeList(List<ActuatorRuntime> actuatorRuntimeList) {
        this.equipmentRunTime.setActuatorRuntimeList(Collections.unmodifiableList(actuatorRuntimeList));
        for (ActuatorRuntime actuatorRuntime : actuatorRuntimeList) {
            actuatorMap.put(actuatorRuntime.getActuator().getName(), new IActuator() {
                @Override
                public boolean isRun() {
                    return actuatorRuntime.isActivated();
                }

                @Override
                public void start() {
                    if (actuatorRuntime.isActivated()) {
                        return;
                    }
                    actuatorRuntime.setActivated(true);

                    addCommandCallback(new CommandCallback<>(
                            Unpooled.buffer()
                                    .writeByte(0x06)
                                    .writeByte(0x01)
                                    .writeInt(actuatorRuntime.getEmbeddedId())
                                    .writeBoolean(true)
                    ));
                }

                @Override
                public void stop() {
                    if (!actuatorRuntime.isActivated()) {
                        return;
                    }
                    actuatorRuntime.setActivated(false);

                    addCommandCallback(new CommandCallback<>(
                            Unpooled.buffer()
                                    .writeByte(0x06)
                                    .writeByte(0x01)
                                    .writeInt(actuatorRuntime.getEmbeddedId())
                                    .writeBoolean(false)
                    ));
                }
            });
        }
    }

    public void setDataTypeRuntimeList(List<DataTypeRunTime> dataTypeRuntimeList) {
        this.equipmentRunTime.setDataTypeRuntimeList(Collections.unmodifiableList(dataTypeRuntimeList));
        for (DataTypeRunTime dataTypeRunTime : dataTypeRuntimeList) {
            dataTypeMap.put(dataTypeRunTime.getDataType().getName(), new IDataType() {
                @Override
                public float getValue() {
                    return dataTypeRunTime.getValue();
                }

                @Override
                public void setDataState(com.til.water_detection.script.DataState dataState) {
                    DataState state = DataState.valueOf(dataState.name());
                    if (dataTypeRunTime.getDataState().equals(state)) {
                        return;
                    }
                    dataTypeRunTime.setDataState(state);
                }

                @Override
                public com.til.water_detection.script.DataState getDataState() {
                    return com.til.water_detection.script.DataState.valueOf(dataTypeRunTime.getDataState().name());
                }
            });
        }
    }

    @Override
    public @Nullable IActuator getActuator(String name) {
        return actuatorMap.get(name);
    }

    @Override
    public @Nullable IDataType getDataType(String name) {
        return dataTypeMap.get(name);
    }

    @Override
    public Iterable<IActuator> getActuators() {
        return actuatorMap.values();
    }

    @Override
    public Iterable<IDataType> getDataTypes() {
        return dataTypeMap.values();
    }

    @Override
    public boolean haveUpdate() {
        boolean haveUpdate = this.haveUpdate;
        this.haveUpdate = false;
        return haveUpdate;
    }
}
