package com.til.water_detection.wab.socket_handler;

import com.til.water_detection.data.*;
import com.til.water_detection.data.run_time.ActuatorRuntime;
import com.til.water_detection.data.run_time.DataTypeRunTime;
import com.til.water_detection.data.run_time.LoginData;
import com.til.water_detection.data.state.DataState;
import com.til.water_detection.data.state.ResultType;
import com.til.water_detection.data.util.FinalByte;
import com.til.water_detection.data.util.FinalString;
import com.til.water_detection.wab.service.*;
import com.til.water_detection.wab.socket_data.CommandCallback;
import com.til.water_detection.wab.socket_data.EquipmentSocketContext;
import com.til.water_detection.wab.socket_data.ReturnPackage;
import com.til.water_detection.wab.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

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
    @Resource
    private IRuleService ruleService;
    @Resource
    private ICommandService commandService;

    @Override
    protected EquipmentSocketContext mackSocketContext(WebSocketSession session) throws IOException {
        EquipmentSocketContext equipmentSocketContext = new EquipmentSocketContext(session);

        LoginData loginData = (LoginData) session.getAttributes().get(FinalString.LOGIN_DATA);

        String equipmentName = loginData.getEquipment();

        List<String> dataTypeList = loginData.getDataNameList();
        List<String> actuatorList = loginData.getActuatorNameList();

        Equipment equipment = equipmentService.getEquipmentByName(equipmentName);
        if (equipment == null) {
            equipmentService.registerEquipment(equipmentName);
            equipment = equipmentService.getEquipmentByName(equipmentName);
            assert equipment != null;
        }

        for (EquipmentSocketContext context : getSocketContext()) {
            if (context.getEquipmentRunTime().getEquipment().getId() == equipment.getId()) {
                session.close();
            }
        }

        equipmentSocketContext.setEquipment(equipment);

        List<DataTypeRunTime> dataTypeRunTimeList = new ArrayList<>();

        for (int i = 0; i < dataTypeList.size(); i++) {
            String name = dataTypeList.get(i);
            DataType dataType = dataTypeService.getDataTypeByName(name);
            if (dataType == null) {
                dataTypeService.registerDataType(name);
                dataType = dataTypeService.getDataTypeByName(name);
                assert dataType != null;
            }
            int equipmentId = equipmentSocketContext.getEquipmentRunTime().getEquipment().getId();
            int dataTypeId = dataType.getId();
            Rule rule = ruleService.getRuleByLimitId(equipmentId, dataTypeId);
            if (rule == null) {
                ruleService.registerRuleSimple(equipmentId, dataTypeId);
                rule = ruleService.getRuleByLimitId(equipmentId, dataTypeId);
                assert rule != null;
            }
            dataTypeRunTimeList.add(new DataTypeRunTime(0, DataState.NORMAL, i, dataType, rule));
        }

        equipmentSocketContext.setDataTypeRuntimeList(dataTypeRunTimeList);


        List<ActuatorRuntime> actuatorRuntimeList = new ArrayList<>();

        for (int i = 0; i < actuatorList.size(); i++) {
            String name = actuatorList.get(i);

            Actuator actuator = actuatorService.getActuatorByName(name);

            if (actuator == null) {
                actuatorService.registerActuator(name);
                actuator = actuatorService.getActuatorByName(name);
                assert actuator != null;
            }

            actuatorRuntimeList.add(new ActuatorRuntime(false, i, actuator));
        }

        equipmentSocketContext.setActuatorRuntimeList(actuatorRuntimeList);

        for (DataTypeRunTime dataTypeRunTime : equipmentSocketContext.getEquipmentRunTime().getDataTypeRuntimeList()) {

            Rule rule = dataTypeRunTime.getRule();

            equipmentSocketContext.addCommandCallback(new CommandCallback<>(
                    Unpooled.buffer()
                            .writeByte(FinalByte.WRITE)
                            .writeByte(FinalByte.RULE)
                            .writeInt(dataTypeRunTime.getEmbeddedId())
                            .writeInt((int) (rule.getExceptionUpper() * 10000))
                            .writeInt((int) (rule.getWarnUpper() * 10000))
                            .writeInt((int) (rule.getWarnLower() * 10000))
                            .writeInt((int) (rule.getExceptionLower() * 10000))
            ) {
            });

        }

        /*  boolean isDeBug = (boolean) session.getAttributes().get("isDeBug");
        if (isDeBug) {
            return equipmentSocketContext;
        }

        equipmentSocketContext.addCommandCallback(new CommandCallback<>(
                Unpooled.buffer()
                        .writeByte(FinalByte.READ)
                        .writeByte(FinalByte.EQUIPMENT)
                        .array()
        ) {
            @Override
            public void successCallback(ByteBuf byteBuf, EquipmentSocketContext socketContext) throws IOException {
                String equipmentNameString = ByteBufUtil.readString(byteBuf, null);

                Equipment equipment = equipmentService.getEquipmentByName(equipmentNameString);
                if (equipment == null) {
                    equipmentService.registerEquipment(equipmentNameString);
                    equipment = equipmentService.getEquipmentByName(equipmentNameString);
                }

                for (EquipmentSocketContext context : getSocketContext()) {
                    if (context.getEquipmentRunTime().getEquipment().getId() == equipment.getId()) {
                        session.close();
                    }
                }


                assert equipment != null;
                socketContext.setEquipment(equipment);
            }
        });
*/
        /*equipmentSocketContext.addCommandCallback(new CommandCallback<>(
                Unpooled.buffer()
                        .writeByte(FinalByte.GET)
                        .writeByte(FinalByte.DATA_TYPE_LIST)
                        .array()
        ) {
            @Override
            public void successCallback(ByteBuf byteBuf, EquipmentSocketContext socketContext) throws IOException {
                super.successCallback(byteBuf, socketContext);

                int l = byteBuf.readInt();

                byte[] dataTypeName = new byte[1024];
                List<String> dataTypeNameList = new ArrayList<>(l);

                int nameLen = 0;
                for (int i = 0; i < l; i++) {
                    byte b = byteBuf.readByte();
                    switch (b) {
                        case '\0':
                            dataTypeNameList.add(new String(dataTypeName, 0, nameLen, StandardCharsets.UTF_8));
                            nameLen = 0;
                            break;
                        default:
                            dataTypeName[nameLen++] = b;
                            break;
                    }
                }

                List<DataTypeRunTime> dataTypeRunTimeList = new ArrayList<>();

                for (int i = 0; i < dataTypeNameList.size(); i++) {
                    String name = dataTypeNameList.get(i);
                    DataType dataType = dataTypeService.getDataTypeByName(name);
                    if (dataType == null) {
                        dataTypeService.registerDataType(name);
                        dataType = dataTypeService.getDataTypeByName(name);
                        assert dataType != null;
                    }
                    int equipmentId = socketContext.getEquipmentRunTime().getEquipment().getId();
                    int dataTypeId = dataType.getId();
                    Rule rule = ruleService.getRuleByLimitId(equipmentId, dataTypeId);
                    if (rule == null) {
                        ruleService.registerRuleSimple(equipmentId, dataTypeId);
                        rule = ruleService.getRuleByLimitId(equipmentId, dataTypeId);
                        assert rule != null;
                    }
                    dataTypeRunTimeList.add(new DataTypeRunTime(0, DataState.NORMAL, i, dataType, rule));
                }

                socketContext.setDataTypeRuntimeList(dataTypeRunTimeList);

            }
        });
*/
        /*equipmentSocketContext.addCommandCallback(new CommandCallback<>(
                Unpooled.buffer()
                        .writeByte(FinalByte.GET)
                        .writeByte(FinalByte.ACTUATOR_LIST)
                        .array()
        ) {
            @Override
            public void successCallback(ByteBuf byteBuf, EquipmentSocketContext socketContext) throws IOException {

                super.successCallback(byteBuf, socketContext);

                int l = byteBuf.readInt();
                List<String> actuatorNameList = new ArrayList<>(l);
                for (int i = 0; i < l; i++) {
                    actuatorNameList.add(ByteBufUtil.readString(byteBuf, null));
                }

                List<ActuatorRuntime> actuatorRuntimeList = new ArrayList<>();

                for (int i = 0; i < actuatorNameList.size(); i++) {
                    String name = actuatorNameList.get(i);

                    Actuator actuator = actuatorService.getActuatorByName(name);

                    if (actuator == null) {
                        actuatorService.registerActuator(name);
                        actuator = actuatorService.getActuatorByName(name);
                        assert actuator != null;
                    }

                    actuatorRuntimeList.add(new ActuatorRuntime(false, i, actuator));
                }

                socketContext.setActuatorRuntimeList(actuatorRuntimeList);
            }
        });*/

        equipmentSocketContext.addCommandCallback(new CommandCallback<>(
                Unpooled.buffer()
                        .writeByte(FinalByte.SYNC_END)
        ) /*{
            @Override
            public void successCallback(ByteBuf byteBuf, EquipmentSocketContext socketContext) throws IOException {
                super.successCallback(byteBuf, socketContext);

                boolean complete = socketContext.getEquipmentRunTime().getEquipment() == null;
                complete = complete && socketContext.getEquipmentRunTime().getActuatorRuntimeList() == null;
                complete = complete && socketContext.getEquipmentRunTime().getDataTypeRuntimeList() == null;
                if (!complete) {

                    try {
                        socketContext.getWebSocketSession().close(CloseStatus.NOT_ACCEPTABLE.withReason("初始化失败"));
                    } catch (IOException e) {
                        logger.info("初始化完成时验证失败：", e);
                    }

                    return;
                }

                socketContext.initEnd();

                for (DataTypeRunTime dataTypeRunTime : socketContext.getEquipmentRunTime().getDataTypeRuntimeList()) {

                    Rule rule = dataTypeRunTime.getRule();

                    equipmentSocketContext.addCommandCallback(new CommandCallback<>(
                            Unpooled.buffer()
                                    .writeByte(FinalByte.WRITE)
                                    .writeByte(FinalByte.RULE)
                                    .writeInt(dataTypeRunTime.getEmbeddedId())
                                    .writeInt((int) (rule.getExceptionUpper() * 10000))
                                    .writeInt((int) (rule.getWarnUpper() * 10000))
                                    .writeInt((int) (rule.getWarnLower() * 10000))
                                    .writeInt((int) (rule.getExceptionLower() * 10000))
                                    .array()
                    ) {
                    });

                }

            }
        }*/);


       /* equipmentSocketContext.addCommandCallback(new CommandCallback<>(FinalString.READ, FinalString.EQUIPMENT) {
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


        equipmentSocketContext.addCommandCallback(new CommandCallback<>(FinalString.GET, FinalString.DATA_TYPE_LIST) {

            @Override
            public void successCallback(String[] strings, EquipmentSocketContext socketContext) {

                Map<Integer, DataType> dataTypeMap = new HashMap<>();
                for (String string : new HashSet<>(List.of(strings))) {
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

        equipmentSocketContext.addCommandCallback(new CommandCallback<>(FinalString.GET, FinalString.ACTUATOR_LIST) {

            @Override
            public void successCallback(String[] strings, EquipmentSocketContext socketContext) {
                Map<Integer, Actuator> actuatorMap = new HashMap<>();
                for (String string : new HashSet<>(List.of(strings))) {
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
                if (!complete) {

                    try {
                        socketContext.getWebSocketSession().close(CloseStatus.NOT_ACCEPTABLE.withReason("初始化失败"));
                    } catch (IOException e) {
                        logger.info("初始化完成时验证失败：", e);
                    }

                    return;
                }

                AtomicBoolean fail = new AtomicBoolean(false);


                for (Map.Entry<Integer, DataType> entry : equipmentSocketContext.getDataTypeMap().entrySet()) {
                    DataType value = entry.getValue();

                    Rule rule = ruleService.getRuleByLimitId(socketContext.getEquipment().getId(), entry.getValue().getId());
                    if (rule == null) {
                        ruleService.registerRuleSimple(socketContext.getEquipment().getId(), entry.getValue().getId());
                        rule = ruleService.getRuleByLimitId(socketContext.getEquipment().getId(), entry.getValue().getId());
                        assert rule != null;
                    }


                    equipmentSocketContext.addCommandCallback(new CommandCallback<>(
                            FinalString.WRITE,
                            FinalString.RULE,
                            value.getName(),
                            String.valueOf(rule.getExceptionUpper()),
                            String.valueOf(rule.getWarnUpper()),
                            String.valueOf(rule.getWarnLower()),
                            String.valueOf(rule.getExceptionLower())
                    ) {
                        @Override
                        public void failCallback(String[] strings, EquipmentSocketContext socketContext) {
                            fail.set(true);
                        }
                    });

                    for (Command command : commandService.getCommandByRuleId(rule.getId())) {
                        //write command {ruleId}     {dataTypeName} {actuatorName} {commandTrigger} {upper} {low}

                        Actuator actuator = actuatorService.getActuatorById(command.getActuatorId());

                        if (actuator == null) {
                            continue;
                        }

                        equipmentSocketContext.addCommandCallback(new CommandCallback<>(
                                FinalString.WRITE,
                                FinalString.COMMAND,
                                String.valueOf(command.getId()),
                                entry.getValue().getName(),
                                actuator.getName(),
                                String.valueOf(command.getCommandTrigger()),
                                String.valueOf(command.getUpper()),
                                String.valueOf(command.getLow())
                        ) {
                            @Override
                            public void failCallback(String[] strings, EquipmentSocketContext socketContext) {
                                fail.set(true);
                            }
                        });
                    }
                }


                equipmentSocketContext.addCommandCallback(new CommandCallback<>(FinalString.SYNC_END) {
                    @Override
                    public void successCallback(String[] strings, EquipmentSocketContext socketContext) {
                        if (!fail.get()) {
                            socketContext.initEnd();
                            return;
                        }
                        try {
                            socketContext.getWebSocketSession().close(CloseStatus.NOT_ACCEPTABLE.withReason("初始化失败"));
                        } catch (IOException e) {
                            logger.info("同步数据时失败:", e);
                        }
                    }

                    @Override
                    public void failCallback(String[] strings, EquipmentSocketContext socketContext) {
                        try {
                            socketContext.getWebSocketSession().close(CloseStatus.NOT_ACCEPTABLE.withReason("初始化失败"));
                        } catch (IOException e) {
                            logger.info("同步数据时失败:", e);
                        }
                    }
                });


            }

            @Override
            public void failCallback(String[] strings, EquipmentSocketContext socketContext) {
                try {
                    socketContext.getWebSocketSession().close(CloseStatus.NOT_ACCEPTABLE.withReason("初始化失败"));
                } catch (IOException e) {
                    logger.info("初始化完成时验证失败:", e);
                }
            }
        });
*/

        return equipmentSocketContext;
    }

    @Override
    protected ReturnPackage command(ByteBuf byteBuf, EquipmentSocketContext equipmentSocketContext) {

        byte b = byteBuf.readByte();

        switch (b) {
            case FinalByte.S_WRITE -> {
                byte tag = byteBuf.readByte();
                switch (tag) {
                    case FinalByte.S_RULE -> {
                        int index = byteBuf.readInt();
                        DataTypeRunTime dataTypeRunTime = equipmentSocketContext.getEquipmentRunTime().getDataTypeRuntimeList().get(index);
                        Rule rule = dataTypeRunTime.getRule();
                        Rule nRule = new Rule(
                                0,
                                0,
                                0,
                                (float) ((double) byteBuf.readInt() / 10000d),
                                (float) ((double) byteBuf.readInt() / 10000d),
                                (float) ((double) byteBuf.readInt() / 10000d),
                                (float) ((double) byteBuf.readInt() / 10000d),
                                rule.isWarnSendMessage(),
                                rule.isExceptionSendMessage());
                        ruleService.updateById(rule.getId(), nRule);
                        dataTypeRunTime.setRule(ruleService.getRuleById(rule.getId()));
                        return new ReturnPackage(ResultType.SUCCESSFUL, null);
                    }
                    case FinalByte.S_EQUIPMENT_NAME -> {
                        String s = ByteBufUtil.readString(byteBuf, null);

                        if (s.isEmpty() || s.length() > 30) {
                            return new ReturnPackage(ResultType.ERROR, null);
                        }

                        if (s.equals(equipmentSocketContext.getEquipmentRunTime().getEquipment().getName())) {
                            return new ReturnPackage(ResultType.SUCCESSFUL, null);
                        }

                        Equipment equipment = equipmentService.getEquipmentByName(s);

                        if (equipment != null) {
                            return new ReturnPackage(ResultType.FAIL, null);
                        }

                        equipmentService.updateEquipmentAnotherNameById(equipmentSocketContext.getEquipmentRunTime().getEquipment().getId(), s);

                        equipmentSocketContext.getEquipmentRunTime().getEquipment().setName(s);

                        return new ReturnPackage(ResultType.SUCCESSFUL, null);

                    }
                    default -> {
                        return new ReturnPackage(ResultType.ERROR, null);
                    }
                }
            }
            case FinalByte.S_REPORTING -> {
                byte tag = byteBuf.readByte();
                switch (tag) {
                    case FinalByte.S_DATA -> {
                        int index = byteBuf.readInt();

                        DataTypeRunTime dataTypeRunTime = equipmentSocketContext.getEquipmentRunTime().getDataTypeRuntimeList().get(index);
                        DataType dataType = dataTypeRunTime.getDataType();

                        equipmentService.updateEquipmentTimeById(equipmentSocketContext.getEquipmentRunTime().getEquipment().getId());
                        float value = (float) ((double) (byteBuf.readInt()) / 10000d);
                        dataService.addData(new Data(0L, equipmentSocketContext.getEquipmentRunTime().getEquipment().getId(), dataType.getId(), null, value));

                        dataTypeRunTime.setDataState(dataTypeRunTime.getRule().ofDataState(value));

                        return new ReturnPackage(ResultType.SUCCESSFUL, null);
                    }
                    case FinalByte.S_GPS -> {

                        float longitude = (float) ((double) byteBuf.readInt() / 10000d);
                        float latitude = (float) ((double) byteBuf.readInt() / 10000d);

                        Equipment equipment = equipmentSocketContext.getEquipmentRunTime().getEquipment();

                        equipmentService.updateEquipmentTimeById(equipment.getId());
                        equipmentService.updateEquipmentPosById(equipment.getId(), longitude, latitude);
                        equipment.setUpTime(new Timestamp(System.currentTimeMillis()));

                        if (equipment.isElectronicFence() && equipment.getLongitude() == 0 && equipment.getLatitude() == 0) {
                            equipmentService.updateEquipmentFencePosById(equipment.getId(), true, longitude, latitude);
                        }

                        equipmentSocketContext.getEquipmentRunTime().setEquipment(equipmentService.getEquipmentById(equipment.getId()));

                        return new ReturnPackage(ResultType.SUCCESSFUL, null);

                    }
                    case FinalByte.S_DATA_LIST -> {
                        equipmentService.updateEquipmentTimeById(equipmentSocketContext.getEquipmentRunTime().getEquipment().getId());
                        for (DataTypeRunTime dataTypeRunTime : equipmentSocketContext.getEquipmentRunTime().getDataTypeRuntimeList()) {
                            float value = (float) ((double) byteBuf.readInt() / 10000d);
                            dataService.addData(new Data(0L, equipmentSocketContext.getEquipmentRunTime().getEquipment().getId(), dataTypeRunTime.getDataType().getId(), null, value));
                            dataTypeRunTime.setDataState(dataTypeRunTime.getRule().ofDataState(value));
                        }
                        return new ReturnPackage(ResultType.SUCCESSFUL, null);
                    }
                    case FinalByte.S_ACTUATOR_LIST -> {
                        for (ActuatorRuntime actuatorRuntime : equipmentSocketContext.getEquipmentRunTime().getActuatorRuntimeList()) {
                            boolean run = byteBuf.readBoolean();
                            actuatorRuntime.setActivated(run);
                        }
                        return new ReturnPackage(ResultType.SUCCESSFUL, null);
                    }

                    default -> {
                        return new ReturnPackage(ResultType.ERROR, null);
                    }
                }
            }
            case FinalByte.S_TIME -> {
                return new ReturnPackage(ResultType.SUCCESSFUL, Unpooled.buffer().writeLong(System.currentTimeMillis()));
            }
            default -> {
                return new ReturnPackage(ResultType.ERROR, null);
            }
        }

    }
}
