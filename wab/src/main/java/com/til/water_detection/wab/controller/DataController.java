package com.til.water_detection.wab.controller;

import com.til.water_detection.data.*;
import com.til.water_detection.wab.service.IDataService;
import com.til.water_detection.wab.service.IDataTypeService;
import com.til.water_detection.wab.service.IEquipmentService;
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
    public Result<Void> addDataSimple(@RequestParam int equipmentId, @RequestParam int dataTypeId, @RequestParam float value) {
        return addData(new Data(0, equipmentId, dataTypeId, null, value));
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

    /*@Deprecated
    @PostMapping("/getDataMapFromEquipmentIdArray")
    public Result<Map<Integer, List<Data>>> getDataMapFromEquipmentIdArray(
            @RequestBody int[] equipmentIdArray,
            @RequestParam int dataTypeId,
            @RequestParam Timestamp start,
            @RequestParam Timestamp end
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
    }*/

   /* @Deprecated
    @PostMapping("/getDataMapFromDataTypeIdArray")
    public Result<Map<Integer, List<Data>>> getDataMapFromDataTypeIdArray(
            @RequestParam int equipmentId,
            @RequestBody int[] dataTypeIdArray,
            @RequestParam Timestamp start,
            @RequestParam Timestamp end
    ) {
        Equipment equipmentById = equipmentService.getEquipmentById(equipmentId);
        if (equipmentById == null) {
            return Result.fail("equipmentId is null");
        }
        List<Data> dataMapFromDataTypeIdArray = dataService.getDataMapFromDataTypeIdArray(equipmentId, dataTypeIdArray, start, end);
        Map<Integer, List<Data>> integerListMap = new HashMap<>();
        for (Data data : dataMapFromDataTypeIdArray) {
            if (!integerListMap.containsKey(data.getDataTypeId())) {
                integerListMap.put(data.getDataTypeId(), new ArrayList<>());
            }
            integerListMap.get(data.getDataTypeId()).add(data);
        }
        return new Result<>(ResultType.SUCCESSFUL, null, integerListMap);
    }*/

    @PostMapping("/getDataToDataSheet")
    public Result<DataSheet> getDataToDataSheet(@RequestBody DataFilter dataFilter) {
        DataType dataType = dataTypeService.getDataTypeById(dataFilter.getDataTypeId());
        if (dataType == null) {
            return Result.fail("dataTypeId is null");
        }
        long endTime = dataFilter.getEndTime().getTime();
        long startTime = dataFilter.getStartTime().getTime();
        long processTime = endTime - startTime;

        long stepByStep = Math.max(dataFilter.getTimeStep(), 10) * 1000L;
        int grid = (int) (processTime / stepByStep);

        if (grid > 10000) {
            grid = 10000;
            stepByStep = processTime / grid;
        }

        List<Timestamp> timestampList = new ArrayList<>(grid);
        List<Long> timestampListLong = new ArrayList<>(grid);
        for (int i = 0; i < grid; i++) {
            timestampList.add(new Timestamp(startTime + (i + 1) * stepByStep));
            timestampListLong.add(startTime + (i + 1) * stepByStep);
        }

        if (dataFilter.getEquipmentIdArray().length == 0) {
            return new Result<>(ResultType.FAIL, null, new DataSheet(
                    dataType,
                    dataFilter.getTimeStep(),
                    dataFilter.getStartTime(),
                    dataFilter.getEndTime(),
                    new ArrayList<>(),
                    timestampList,
                    new ArrayList<>()
            ));
        }

        List<Equipment> equipmentList = equipmentService.getEquipmentByIdArray(dataFilter.getEquipmentIdArray())
                .stream()
                .sorted(Comparator.comparingInt(Equipment::getId))
                .toList();

        List<List<Float>> value = new ArrayList<>(equipmentList.size());
        for (int i = 0; i < equipmentList.size(); i++) {
            value.add(new ArrayList<>(timestampList.size()));
        }


        List<Data> dataList = dataService.getDataMapFromEquipmentIdArray(
                dataFilter.getEquipmentIdArray(),
                dataType.getId(),
                dataFilter.getStartTime(),
                dataFilter.getEndTime());

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
                        (int) stepByStep / 1000,
                        dataFilter.getStartTime(),
                        dataFilter.getEndTime(),
                        equipmentList,
                        timestampList,
                        value
                )
        );
    }
}
