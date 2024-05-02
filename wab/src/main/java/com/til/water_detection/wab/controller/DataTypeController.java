package com.til.water_detection.wab.controller;


import com.til.water_detection.data.DataType;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import com.til.water_detection.wab.service.IDataTypeService;
import com.til.water_detection.wab.util.FinalString;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
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

    /***
     * 添加用户自定义的数据 成功时需要重新拉取所有数据类型
     */
    @PostMapping("/register")
    public Result<Void> register() {
        dataTypeService.addDataType();
        return Result.successful(null);
    }

    @DeleteMapping("/removeDataTypeById")
    public Result<Void> removeDataTypeById(int id) {
        int i = dataTypeService.removeDataTypeById(id);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, i + "条数据被删除", null);
    }

    @GetMapping("/getAllDataType")
    public Result<List<DataType>> getAllDataType() {
        List<DataType> dataTypeListByUserId = dataTypeService.getAllDataType();
        return new Result<>(ResultType.SUCCESSFUL, null, dataTypeListByUserId);
    }

    @GetMapping("/getDataTypeById")
    public Result<DataType> getDataTypeById(int id) {
        DataType dataType = dataTypeService.getDataTypeById(id);
        if (dataType == null) {
            return Result.fail("未找到对应id的数据类型");
        }
        return new Result<>(ResultType.SUCCESSFUL, null, dataType);
    }

    @GetMapping("/getDataTypeByIdArray")
    public Result<List<DataType>> getDataTypeByIdArray(int[] id) {
        return new Result<>(ResultType.SUCCESSFUL, null, dataTypeService.getDataTypeByIdArray(id));
    }

    @PutMapping("/updateDataTypeAnotherName")
    public Result<Void> updateDataTypeAnotherName(int id, @Param(FinalString.VERIFY_1_30) String anotherName) {
        int i = dataTypeService.updateDataTypeAnotherName(id, anotherName);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, i + "条数据被更改", null);
    }


}
