package com.til.water_detection.wab.socket_handler;

import com.til.water_detection.data.*;
import com.til.water_detection.data.run_time.ActuatorRuntime;
import com.til.water_detection.data.run_time.DataTypeRunTime;
import com.til.water_detection.data.run_time.EquipmentRunTime;
import com.til.water_detection.data.run_time.LoginData;
import com.til.water_detection.data.state.DataState;
import com.til.water_detection.data.state.ResultType;
import com.til.water_detection.data.util.FinalByte;
import com.til.water_detection.data.util.FinalString;
import com.til.water_detection.wab.service.*;
import com.til.water_detection.wab.socket_data.ComEntry;
import com.til.water_detection.wab.socket_data.CommandCallback;
import com.til.water_detection.wab.socket_data.EquipmentSocketContext;
import com.til.water_detection.wab.socket_data.ReturnPackage;
import com.til.water_detection.wab.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class EquipmentSocketHandler extends CommandSocketHandlerBasics<EquipmentSocketContext> {
    @Resource
    @Getter
    private IEquipmentService equipmentService;
    @Resource
    @Getter
    private IDataService dataService;
    @Resource
    @Getter
    private IDataTypeService dataTypeService;
    @Resource
    @Getter
    private IActuatorService actuatorService;
    @Resource
    @Getter
    private IRuleService ruleService;
    @Resource
    @Getter
    private ICommandService commandService;

    private final List<ComEntry<EquipmentSocketContext>> rootComEntryList;

    private final List<ComEntry<EquipmentSocketContext>> weiteComEntryList;

    private final List<ComEntry<EquipmentSocketContext>> reportingComEntryList;

    {
        reportingComEntryList = List.of(new ComEntry<>(FinalByte.S_DATA, (source, output, tag, c) -> {
            int index = source.readInt();

            DataTypeRunTime dataTypeRunTime = c.getEquipmentRunTime().getDataTypeRuntimeList().get(index);
            DataType dataType = dataTypeRunTime.getDataType();

            equipmentService.updateEquipmentTimeById(c.getEquipmentRunTime().getEquipment().getId());
            float value = (float) ((double) (source.readInt()) / 10000d);
            dataService.addData(new Data(0L, c.getEquipmentRunTime().getEquipment().getId(), dataType.getId(), null, value));

            dataTypeRunTime.setDataState(dataTypeRunTime.getRule().ofDataState(value));
            output.writeByte(ResultType.SUCCESSFUL.getState());
        }), new ComEntry<>(FinalByte.S_GPS, (source, output, tag, c) -> {
            float longitude = (float) ((double) source.readInt() / 10000d);
            float latitude = (float) ((double) source.readInt() / 10000d);

            Equipment equipment = c.getEquipmentRunTime().getEquipment();

            equipmentService.updateEquipmentTimeById(equipment.getId());
            equipmentService.updateEquipmentPosById(equipment.getId(), longitude, latitude);
            equipment.setUpTime(new Timestamp(System.currentTimeMillis()));

            if (equipment.isElectronicFence() && equipment.getLongitude() == 0 && equipment.getLatitude() == 0) {
                equipmentService.updateEquipmentFencePosById(equipment.getId(), true, longitude, latitude, equipment.getFenceRange());
            }

            c.getEquipmentRunTime().setEquipment(equipmentService.getEquipmentById(equipment.getId()));
            output.writeByte(ResultType.SUCCESSFUL.getState());
        }), new ComEntry<>(FinalByte.S_DATA_LIST, (source, output, tag, c) -> {
            equipmentService.updateEquipmentTimeById(c.getEquipmentRunTime().getEquipment().getId());
            for (DataTypeRunTime dataTypeRunTime : c.getEquipmentRunTime().getDataTypeRuntimeList()) {
                float value = (float) ((double) source.readInt() / 10000d);
                dataService.addData(new Data(0L, c.getEquipmentRunTime().getEquipment().getId(), dataTypeRunTime.getDataType().getId(), null, value));
                dataTypeRunTime.setDataState(dataTypeRunTime.getRule().ofDataState(value));
                dataTypeRunTime.setValue(value);
            }
            output.writeByte(ResultType.SUCCESSFUL.getState());
        }), new ComEntry<>(FinalByte.S_ACTUATOR_LIST, (source, output, tag, c) -> {
            for (ActuatorRuntime actuatorRuntime : c.getEquipmentRunTime().getActuatorRuntimeList()) {
                boolean run = source.readBoolean();
                actuatorRuntime.setActivated(run);
            }
            output.writeByte(ResultType.SUCCESSFUL.getState());
        }));
        weiteComEntryList = List.of(new ComEntry<>(FinalByte.S_RULE, (source, output, tag, c) -> {
            int index = source.readInt();
            DataTypeRunTime dataTypeRunTime = c.getEquipmentRunTime().getDataTypeRuntimeList().get(index);
            Rule rule = dataTypeRunTime.getRule();
            Rule nRule = new Rule(0, 0, 0, (float) ((double) source.readInt() / 10000d), (float) ((double) source.readInt() / 10000d), (float) ((double) source.readInt() / 10000d), (float) ((double) source.readInt() / 10000d), rule.isWarnSendMessage(), rule.isExceptionSendMessage());
            ruleService.updateById(rule.getId(), nRule);
            dataTypeRunTime.setRule(ruleService.getRuleById(rule.getId()));
            output.writeByte(ResultType.SUCCESSFUL.getState());
        }), new ComEntry<>(FinalByte.S_EQUIPMENT_NAME, (source, output, tag, c) -> {
            String s = ByteBufUtil.readString(source, null);

            if (s.isEmpty() || s.length() > 30) {
                output.writeByte(ResultType.ERROR.getState());
                return;
            }

            if (s.equals(c.getEquipmentRunTime().getEquipment().getName())) {
                output.writeByte(ResultType.ERROR.getState());
                return;
            }

            Equipment equipment = equipmentService.getEquipmentByName(s);

            if (equipment != null) {
                output.writeByte(ResultType.ERROR.getState());
                return;
            }

            equipmentService.updateEquipmentAnotherNameById(c.getEquipmentRunTime().getEquipment().getId(), s);

            c.getEquipmentRunTime().getEquipment().setName(s);
            output.writeByte(ResultType.SUCCESSFUL.getState());
        }));
        rootComEntryList = List.of(new ComEntry<>(FinalByte.S_WRITE, (source, output, tag, c) -> generalDecode(source, output, tag, c, weiteComEntryList)), new ComEntry<>(FinalByte.S_REPORTING, (source, output, tag, c) -> generalDecode(source, output, tag, c, reportingComEntryList)), new ComEntry<>(FinalByte.S_TIME, (source, output, tag, c) -> {
            output.writeByte(ResultType.SUCCESSFUL.getState());
            output.writeLong(System.currentTimeMillis());
        }));
    }

    private void generalDecode(ByteBuf source, ByteBuf output, Tag tag, EquipmentSocketContext c, List<ComEntry<EquipmentSocketContext>> list) {
        byte b = source.readByte();
        for (ComEntry<EquipmentSocketContext> equipmentSocketContextComEntry : list) {
            if (b == equipmentSocketContextComEntry.getSign()) {
                equipmentSocketContextComEntry.getDecode().decode(source, output, tag, c);
                return;
            }
        }
    }

    @Override
    protected EquipmentSocketContext mackSocketContext(WebSocketSession session) throws IOException {
        EquipmentSocketContext equipmentSocketContext = new EquipmentSocketContext(session, this);

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
                try {
                    context.getWebSocketSession().close();
                } catch (IOException e) {
                    logger.error(e);
                }
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
            dataTypeRunTimeList.add(new DataTypeRunTime(0, DataState.NORMAL, new LinkedList<>(), i, dataType, rule));
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

        return equipmentSocketContext;
    }

    @Override
    protected void command(ByteBuf source, ByteBuf output, Tag tag, EquipmentSocketContext equipmentSocketContext) {
        generalDecode(source, output, tag, equipmentSocketContext, rootComEntryList);
    }

    @Nullable
    public EquipmentRunTime getEquipmentRunTimeById(int id) {
        //noinspection OptionalGetWithoutIsPresent
        return getSocketContext().stream().map(EquipmentSocketContext::getEquipmentRunTime).filter(equipmentRunTime -> equipmentRunTime.getEquipment().getId() == id).findFirst().get();
    }


    @Nullable
    public EquipmentSocketContext getEquipmentSocketContextByid(int id) {
        //noinspection OptionalGetWithoutIsPresent
        return getSocketContext().stream().filter(equipmentRunTime -> equipmentRunTime.getEquipmentRunTime().getEquipment().getId() == id).findFirst().get();
    }
}
