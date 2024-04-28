package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Data;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import com.til.water_detection.wab.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/data")
@Validated
public class DataController {

    @Autowired
    private IDataService dataService;

    @PostMapping("/addData")
    public Result<Void> addData(Data data) {
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
            long start,
            long end
    ) {
        return new Result<>(ResultType.SUCCESSFUL, null, dataService.getData(equipmentId, dataTypeId, start, end));
    }
}
