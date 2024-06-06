package com.til.water_detection.wab.controller;


import com.til.water_detection.data.DataType;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.state.ResultType;
import com.til.water_detection.wab.service.IDataTypeService;
import com.til.water_detection.data.util.FinalString;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dataType")
@Validated
@ResponseBody
public class DataTypeController {

    @Resource
    private IDataTypeService dataTypeService;

    @PostMapping("/register")
    public Result<Void> registerDataType(@RequestParam @Param(FinalString.VERIFY_1_32) String name) {
        int i = dataTypeService.registerDataType(name);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, null, null);
    }

    @DeleteMapping("/removeDataTypeById")
    public Result<Void> removeDataTypeById(@RequestParam int id) {
        int i = dataTypeService.removeDataTypeById(id);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, null, null);
    }

    @PostMapping("/updateDataTypeSuffixById")
    public Result<Void> updateDataTypeSuffixById(@RequestParam int id, @RequestParam String suffix) {
        int i = dataTypeService.updateDataTypeSuffixById(id, suffix);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, null, null);
    }

    @PostMapping("/updateDataTypePercentById")
    public Result<Void> updateDataTypePercentById(@RequestParam int id, @RequestParam boolean percent) {
        int i = dataTypeService.updateDataTypePercentById(id, percent);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, null, null);
    }

    @GetMapping("/getDataTypeById")
    public Result<DataType> getDataTypeById(@RequestParam int id) {
        DataType dataType = dataTypeService.getDataTypeById(id);
        return new Result<>(dataType == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, dataType);
    }

    @GetMapping("/getDataTypeByName")
    public Result<DataType> getDataTypeByName(@RequestParam String name) {
        DataType dataType = dataTypeService.getDataTypeByName(name);
        return new Result<>(dataType == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, dataType);
    }

    @GetMapping("/getAllDataType")
    public Result<List<DataType>> getAllDataType() {
        List<DataType> dataTypeListByUserId = dataTypeService.getAllDataType();
        return new Result<>(ResultType.SUCCESSFUL, null, dataTypeListByUserId);
    }

    @GetMapping("/getDataTypeByIdArray")
    public Result<List<DataType>> getDataTypeByIdArray(@RequestParam int[] id) {
        return new Result<>(ResultType.SUCCESSFUL, null, dataTypeService.getDataTypeByIdArray(id));
    }

    @GetMapping("/getDataTypeByNameArray")
    public Result<List<DataType>> getDataTypeByIdArray(@RequestParam String[] name) {
        return new Result<>(ResultType.SUCCESSFUL, null, dataTypeService.getDataTypeByNameArray(name));
    }
}
