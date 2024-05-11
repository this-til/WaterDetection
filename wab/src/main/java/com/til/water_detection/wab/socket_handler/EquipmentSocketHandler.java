package com.til.water_detection.wab.socket_handler;

import com.til.water_detection.data.*;
import com.til.water_detection.data.util.FinalString;
import com.til.water_detection.wab.service.*;
import com.til.water_detection.wab.socket_data.CommandCallback;
import com.til.water_detection.wab.socket_data.EquipmentSocketContext;
import com.til.water_detection.wab.socket_data.ReturnPackage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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


        return equipmentSocketContext;
    }

    @Override
    protected ReturnPackage command(String[] pack, EquipmentSocketContext equipmentSocketContext) {
        if (!equipmentSocketContext.isInit()) {
            return super.command(pack, equipmentSocketContext);
        }
        switch (pack[0]) {
            case FinalString.REPORT -> {
                if (pack.length < 3) {
                    return new ReturnPackage(ReturnState.FAIL, "指令位数不符合标准");
                }
                String dataTypeName = pack[1];
                DataType dataType = dataTypeService.getDataTypeByName(dataTypeName);
                if (dataType == null) {
                    return new ReturnPackage(ReturnState.FAIL, "未知的数据类型");
                }
                float value;
                try {
                    value = Float.parseFloat(pack[2]);
                } catch (NumberFormatException e) {
                    return new ReturnPackage(ReturnState.FAIL, "数据错误");
                }
                dataService.addData(new Data(0, equipmentSocketContext.getEquipment().getId(), dataType.getId(), null, value));
                return new ReturnPackage(ReturnState.SUCCESSFUL);
            }

            case FinalString.UPDATE -> {
                switch (pack[1]) {
                    case FinalString.RULE -> {
                        if (pack.length < 7) {
                            return new ReturnPackage(ReturnState.FAIL, "指令位数不符合标准");
                        }
                        DataType dataType = dataTypeService.getDataTypeByName(pack[2]);
                        if (dataType == null) {
                            return new ReturnPackage(ReturnState.FAIL, "未知的数据类型");
                        }
                        Rule rule = ruleService.getRuleByLimitId(equipmentSocketContext.getEquipment().getId(), dataType.getId());
                        if (rule == null) {
                            return new ReturnPackage(ReturnState.FAIL, "未知的规则");
                        }
                        float exceptionUpper;
                        float warnUpper;
                        float warnLower;
                        float exceptionLower;
                        try {
                            exceptionUpper = Float.parseFloat(pack[3]);
                            warnUpper = Float.parseFloat(pack[4]);
                            warnLower = Float.parseFloat(pack[5]);
                            exceptionLower = Float.parseFloat(pack[6]);
                        } catch (NumberFormatException e) {
                            return new ReturnPackage(ReturnState.FAIL, "数据错误");
                        }
                        rule.setExceptionLower(exceptionLower);
                        rule.setExceptionUpper(exceptionUpper);
                        rule.setWarnLower(warnLower);
                        rule.setWarnUpper(warnUpper);
                        ruleService.updateById(rule.getId(), rule);
                        return new ReturnPackage(ReturnState.SUCCESSFUL);
                    }

                    case FinalString.COMMAND -> {
                        if (pack.length < 8) {
                            return new ReturnPackage(ReturnState.FAIL, "指令位数不符合标准");
                        }
                        int commandId;

                        try {
                            commandId = Integer.parseInt(pack[2]);
                        } catch (NumberFormatException e) {
                            return new ReturnPackage(ReturnState.FAIL, "ID错误");
                        }

                        Command command = commandService.getCommandById(commandId);

                        if (command == null) {
                            return new ReturnPackage(ReturnState.FAIL, "未知的指令");
                        }

                        DataType dataType = dataTypeService.getDataTypeByName(pack[3]);
                        if (dataType == null) {
                            return new ReturnPackage(ReturnState.FAIL, "未知的数据类型");
                        }

                        Rule rule = ruleService.getRuleByLimitId(equipmentSocketContext.getEquipment().getId(), dataType.getId());
                        if (rule == null) {
                            return new ReturnPackage(ReturnState.FAIL, "未知的规则");
                        }

                        if (rule.getEquipmentId() != equipmentSocketContext.getEquipment().getId()) {
                            return new ReturnPackage(ReturnState.FAIL, "越界");
                        }

                        String actuatorName = pack[4];

                        Actuator actuator = actuatorService.getActuatorByName(actuatorName);
                        if (actuator == null) {
                            return new ReturnPackage(ReturnState.FAIL, "未知的执行器");
                        }
                        String commandTrigger = pack[5];
                        String[] split = commandTrigger.split("\\|");
                        List<CommandTrigger> commandTriggers = new ArrayList<>();
                        for (String s : split) {
                            try {
                                commandTriggers.add(CommandTrigger.valueOf(s));
                            } catch (Exception e) {
                                return new ReturnPackage(ReturnState.FAIL, "未定义的 CommandTrigger");
                            }
                        }
                        int _commandTrigger = CommandTrigger.of(commandTriggers);

                        float upper;
                        float low;

                        try {
                            upper = Float.parseFloat(pack[6]);
                            low = Float.parseFloat(pack[7]);
                        } catch (Exception e) {
                            return new ReturnPackage(ReturnState.FAIL, "数据错误");
                        }
                        command.setRuleId(rule.getId());
                        command.setCommandTrigger(_commandTrigger);
                        command.setActuatorId(actuator.getId());
                        command.setLow(low);
                        command.setUpper(upper);
                        commandService.updateCommand(command.getRuleId(), command);
                        return new ReturnPackage(ReturnState.SUCCESSFUL);
                    }


                }
            }

        }
        return super.command(pack, equipmentSocketContext);
    }
}
