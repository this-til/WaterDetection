package com.til.water_detection.wab.controller;

import com.til.water_detection.data.*;
import com.til.water_detection.wab.service.IDataService;
import com.til.water_detection.wab.service.IDataTypeService;
import com.til.water_detection.wab.service.IEquipmentService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data")
@Validated
@ResponseBody
public class DataController {

    @Resource
    private IDataService dataService;

    @Resource
    private IDataTypeService dataTypeService;

    @Resource
    private IEquipmentService equipmentService;

    @PostMapping("/addData")
    public Result<Void> addData(Data data) {

        DataType dataType = dataTypeService.getDataTypeById(data.getDataTypeId());
        Equipment equipmentById = equipmentService.getEquipmentById(data.getDataTypeId());

        if (dataType == null) {
            return Result.fail("dataTypeId is null");
        }
        if (equipmentById == null) {
            return Result.fail("equipmentId is null");
        }

        int i = dataService.addData(data);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @GetMapping("/getDataById")
    public Result<Data> getDataById(long id) {
        Data dataById = dataService.getDataById(id);
        return new Result<>(dataById == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, dataById);
    }

    @GetMapping("/getAllData")
    public Result<List<Data>> getAllData() {
        return new Result<>(ResultType.SUCCESSFUL, null, dataService.getAllData());
    }

    @GetMapping("/getData")
    public Result<List<Data>> getData(
            int equipmentId,
            int dataTypeId,
            Timestamp start,
            Timestamp end
    ) {

        DataType dataType = dataTypeService.getDataTypeById(dataTypeId);
        Equipment equipmentById = equipmentService.getEquipmentById(equipmentId);

        if (dataType == null) {
            return Result.fail("dataTypeId is null");
        }
        if (equipmentById == null) {
            return Result.fail("equipmentId is null");
        }
        return new Result<>(ResultType.SUCCESSFUL, null, dataService.getData(equipmentId, dataTypeId, start, end));
    }

    @GetMapping("/getDataMapFromEquipmentIdArray")
    public Result<Map<Integer, List<Data>>> getDataMapFromEquipmentIdArray(
            int[] equipmentIdArray,
            int dataTypeId,
            Timestamp start,
            Timestamp end
    ) {
        DataType dataType = dataTypeService.getDataTypeById(dataTypeId);
        if (dataType == null) {
            return Result.fail("dataTypeId is null");
        }
        List<Data> dataMapFromEquipmentIdArray = dataService.getDataMapFromEquipmentIdArray(equipmentIdArray, dataTypeId, start, end);
        Map<Integer, List<Data>> integerListMap = new HashMap<>(dataMapFromEquipmentIdArray.size());
        for (Data data : dataMapFromEquipmentIdArray) {
            if (!integerListMap.containsKey(data.getEquipmentId())) {
                integerListMap.put(data.getEquipmentId(), new ArrayList<>());
            }
            integerListMap.get(data.getEquipmentId()).add(data);
        }
        return new Result<>(ResultType.SUCCESSFUL, null, integerListMap);
    }

    @GetMapping("/getDataMapFromDataTypeIdArray")
    public Result<Map<Integer, List<Data>>> getDataMapFromDataTypeIdArray(
            int equipmentId,
            int[] dataTypeIdArray,
            Timestamp start,
            Timestamp end
    ) {
        Equipment equipmentById = equipmentService.getEquipmentById(equipmentId);
        if (equipmentById == null) {
            return Result.fail("equipmentId is null");
        }
        List<Data> dataMapFromDataTypeIdArray = dataService.getDataMapFromDataTypeIdArray(equipmentId, dataTypeIdArray, start, end);
        Map<Integer, List<Data>> integerListMap = new HashMap<>(dataMapFromDataTypeIdArray.size());
        for (Data data : dataMapFromDataTypeIdArray) {
            if (!integerListMap.containsKey(data.getDataTypeId())) {
                integerListMap.put(data.getDataTypeId(), new ArrayList<>());
            }
            integerListMap.get(data.getDataTypeId()).add(data);
        }
        return new Result<>(ResultType.SUCCESSFUL, null, integerListMap);
    }
}
