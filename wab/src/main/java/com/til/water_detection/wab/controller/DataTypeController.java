package com.til.water_detection.wab.controller;


import com.til.water_detection.data.DataType;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import com.til.water_detection.wab.service.DataTypeService;
import com.til.water_detection.wab.util.FinalString;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data_type")
@Validated
public class DataTypeController {

    @Autowired
    private DataTypeService dataTypeService;

    /***
     * 添加用户自定义的数据 成功时需要重新拉取所有数据类型
     */
    @PostMapping("/register")
    public Result<Void> register(HttpServletRequest request) {
        int userId = (int) request.getAttribute(FinalString.ID);
        dataTypeService.addDataType(userId);
        return Result.successful(null);
    }

    @GetMapping("/getDataTypeByUserId")
    public Result<List<DataType>> getDataTypeByUserId(HttpServletRequest request) {
        int userId = (int) request.getAttribute(FinalString.ID);
        List<DataType> dataTypeListByUserId = dataTypeService.getDataTypeListByUserId(userId);
        return new Result<>(ResultType.SUCCESSFUL, null, dataTypeListByUserId);
    }

    @GetMapping("/getDataTypeById")
    public Result<DataType> getDataTypeById(HttpServletRequest request, int id) {
        int userId = (int) request.getAttribute(FinalString.ID);
        DataType dataType = dataTypeService.getDatatypeById(id, userId);
        if (dataType == null) {
            return Result.fail("未找到对应id的数据类型");
        }
        return new Result<>(ResultType.SUCCESSFUL, null, dataType);
    }

    @PutMapping("/updateDataTypeAnotherNameById")
    public Result<Void> updateDataTypeAnotherNameById(HttpServletRequest request, int id, @Param(FinalString.VERIFY_1_30) String anotherName) {
        int userId = (int) request.getAttribute(FinalString.ID);
        int i = dataTypeService.updateDataTypeAnotherNameById(id, userId, anotherName);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, i + "条数据被更改", null);
    }


}
