package com.til.water_detection.wab.socket_handler;

import com.til.water_detection.data.Actuator;
import com.til.water_detection.data.DataType;
import com.til.water_detection.data.Equipment;
import com.til.water_detection.data.util.FinalString;
import com.til.water_detection.wab.service.IActuatorService;
import com.til.water_detection.wab.service.IDataService;
import com.til.water_detection.wab.service.IDataTypeService;
import com.til.water_detection.wab.service.IEquipmentService;
import com.til.water_detection.wab.socket_data.CommandCallback;
import com.til.water_detection.wab.socket_data.EquipmentSocketContext;
import com.til.water_detection.wab.socket_data.SocketContext;
import jakarta.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Controller
public class EquipmentSocketHandler extends CommandSocketHandlerBasics<EquipmentSocketContext> {
    @Resource
    private IEquipmentService equipmentService;
    @Resource
    private IDataService dataService;
    @Resource
    private IDataTypeService dataTypeService;
    @Resource
    private IActuatorService actuatorService;

    @Override
    protected EquipmentSocketContext mackSocketContext(WebSocketSession session) {
        EquipmentSocketContext equipmentSocketContext = new EquipmentSocketContext(session);

        equipmentSocketContext.addCommandCallback(new CommandCallback<>(FinalString.READ, FinalString.EQUIPMENT) {
            @Override
            public void successCallback(String[] strings, EquipmentSocketContext socketContext) {
                String equipmentName = strings[0];

                Equipment equipment = equipmentService.getEquipmentByName(equipmentName);

                if (equipment == null) {
                    equipmentService.registerEquipment(equipmentName);
                    equipment = equipmentService.getEquipmentByName(equipmentName);
                }

                assert equipment != null;

                socketContext.setEquipment(equipment);
            }
        });


        equipmentSocketContext.addCommandCallback(new CommandCallback<>(FinalString.READ, FinalString.DATA_TYPE_LIST) {

            @Override
            public void successCallback(String[] strings, EquipmentSocketContext socketContext) {

                Map<Integer, DataType> dataTypeMap = new HashMap<>();
                for (String string : strings) {
                    DataType dataType = dataTypeService.getDataTypeByName(string);
                    if (dataType == null) {
                        dataTypeService.registerDataType(string);
                        dataType = dataTypeService.getDataTypeByName(string);
                        assert dataType != null;
                    }
                    dataTypeMap.put(dataType.getId(), dataType);
                }

                socketContext.setDataTypeMap(dataTypeMap);
            }
        });

        equipmentSocketContext.addCommandCallback(new CommandCallback<>(FinalString.READ, FinalString.ACTUATOR_LIST) {

            @Override
            public void successCallback(String[] strings, EquipmentSocketContext socketContext) {
                Map<Integer, Actuator> actuatorMap = new HashMap<>();
                for (String string : strings) {
                    Actuator actuator = actuatorService.getActuatorByName(string);
                    if (actuator == null) {
                        actuatorService.registerActuator(string);
                        actuator = actuatorService.getActuatorByName(string);
                        assert actuator != null;
                    }
                    actuatorMap.put(actuator.getId(), actuator);
                }
                socketContext.setActuatorMap(actuatorMap);
            }
        });

        equipmentSocketContext.addCommandCallback(new CommandCallback<>(FinalString.INIT_END) {
            @Override
            public void successCallback(String[] strings, EquipmentSocketContext socketContext) {
                boolean complete = socketContext.getEquipment() == null;
                complete = complete && socketContext.getActuatorMap() == null;
                complete = complete && socketContext.getDataTypeMap() == null;
                if (complete) {
                    return;
                }
                try {
                    socketContext.getWebSocketSession().close(CloseStatus.NOT_ACCEPTABLE.withReason("初始化失败"));
                } catch (IOException e) {
                    logger.info("初始化完成时验证失败  ", e);
                }
            }

            @Override
            public void failCallback(String[] strings, EquipmentSocketContext socketContext) {
                try {
                    socketContext.getWebSocketSession().close(CloseStatus.NOT_ACCEPTABLE.withReason("初始化失败"));
                } catch (IOException e) {
                    logger.info("初始化完成时验证失败  ", e);
                }
            }
        });

        return equipmentSocketContext;
    }

}
