package com.til.water_detection.wab.controller;

import com.til.water_detection.data.*;
import com.til.water_detection.wab.service.IDataService;
import com.til.water_detection.wab.service.IDataTypeService;
import com.til.water_detection.wab.service.IEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/data")
@Validated
@ResponseBody
public class DataController {

    @Autowired
    private IDataService dataService;

    @Autowired
    private IDataTypeService dataTypeService;

    @Autowired
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
}
