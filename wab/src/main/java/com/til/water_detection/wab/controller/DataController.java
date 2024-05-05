package com.til.water_detection.wab.controller;

import com.til.water_detection.data.*;
import com.til.water_detection.wab.service.IDataService;
import com.til.water_detection.wab.service.IDataTypeService;
import com.til.water_detection.wab.service.IEquipmentService;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

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
    public Result<Void> addData(@RequestBody Data data) {
        int i = dataService.addData(data);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @PostMapping("/addDataSimple")
    public Result<Void> addDataSimple(
            @RequestParam int equipmentId,
            @RequestParam int dataTypeId,
            @RequestParam(required = false) @Nullable Timestamp time,
            @RequestParam float value
    ) {
        return addData(new Data(0, equipmentId, dataTypeId, time, value));
    }

    @PostMapping("/addDataList")
    public Result<Void> addDataList(@RequestBody List<Data> dataList) {
        int i = 0;
        for (Data data : dataList) {
            i += dataService.addData(data);
        }
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, "已经成功插入" + i + "条数据", null);
    }

    @GetMapping("/getDataById")
    public Result<Data> getDataById(@RequestParam long id) {
        Data dataById = dataService.getDataById(id);
        return new Result<>(dataById == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, dataById);
    }

    @GetMapping("/getAllData")
    public Result<List<Data>> getAllData() {
        return new Result<>(ResultType.SUCCESSFUL, null, dataService.getAllData());
    }

    @GetMapping("/getData")
    public Result<List<Data>> getData(
            @RequestParam int equipmentId,
            @RequestParam int dataTypeId,
            @RequestParam Timestamp start,
            @RequestParam Timestamp end
    ) {
        return new Result<>(ResultType.SUCCESSFUL, null, dataService.getData(equipmentId, dataTypeId, start, end));
    }

    @GetMapping("/getDataToDataSheet")
    public Result<DataSheet> getDataToDataSheet(
            @RequestParam int dataTypeId,
            @RequestParam int[] equipmentIdArray,
            @RequestParam int timeStep,
            @RequestParam Timestamp startTime,
            @RequestParam Timestamp endTime
    ) {
        DataType dataType = dataTypeService.getDataTypeById(dataTypeId);
        if (dataType == null) {
            return Result.fail("dataTypeId is null");
        }
        long endTime_long = startTime.getTime();
        long startTime_long = endTime.getTime();
        long processTime = endTime_long - startTime_long;

        long timeStep_long = Math.max(timeStep, 1) * 1000L;
        int grid = (int) (processTime / timeStep_long);

        if (grid > 10000) {
            grid = 10000;
            timeStep_long = processTime / grid;
        }

        List<Timestamp> timestampList = new ArrayList<>(grid);
        List<Long> timestampListLong = new ArrayList<>(grid);
        for (int i = 0; i < grid; i++) {
            timestampList.add(new Timestamp(startTime_long + (i + 1) * timeStep_long));
            timestampListLong.add(startTime_long + (i + 1) * timeStep_long);
        }

        if (equipmentIdArray.length == 0) {
            return new Result<>(ResultType.FAIL, null, new DataSheet(
                    dataType,
                    (int) (timeStep_long / 1000),
                    startTime,
                    endTime,
                    new ArrayList<>(),
                    timestampList,
                    new ArrayList<>()
            ));
        }

        List<Equipment> equipmentList = equipmentService.getEquipmentByIdArray(equipmentIdArray)
                .stream()
                .sorted(Comparator.comparingInt(Equipment::getId))
                .toList();

        List<List<Float>> value = new ArrayList<>(equipmentList.size());
        for (int i = 0; i < equipmentList.size(); i++) {
            value.add(new ArrayList<>(timestampList.size()));
        }


        List<Data> dataList = dataService.getDataMapFromEquipmentIdArray(
                equipmentIdArray,
                dataType.getId(),
                startTime,
                endTime);

        Map<Integer, List<Data>> integerListMap = new HashMap<>();
        for (Equipment equipment : equipmentList) {
            integerListMap.put(equipment.getId(), new ArrayList<>());
        }
        for (Data data : dataList) {
            integerListMap.get(data.getEquipmentId()).add(data);
        }

        for (int i = 0; i < value.size(); i++) {
            List<Float> floats = value.get(i);
            Equipment equipment = equipmentList.get(i);
            List<Data> _dataList = integerListMap.get(equipment.getId())
                    .stream()
                    .sorted(Comparator.comparingLong(t -> t.getTime().getTime()))
                    .toList();

            int _ii = 0;
            for (int ii = 0; ii < timestampListLong.size(); ii++) {

                Float v = null;

                while (true) {

                    if (_ii >= _dataList.size()) {
                        break;
                    }

                    Data data = _dataList.get(_ii);

                    if (data.getTime().getTime() > timestampListLong.get(ii)) {
                        break;
                    }

                    if (v == null) {
                        v = data.getValue();
                    } else {
                        v = (v + data.getValue()) / 2;
                    }

                    _ii++;
                }
                floats.add(v);
            }

        }

        return new Result<>(
                ResultType.SUCCESSFUL,
                null,
                new DataSheet(
                        dataType,
                        (int) timeStep_long / 1000,
                        startTime,
                        endTime,
                        equipmentList,
                        timestampList,
                        value
                )
        );
    }
}
