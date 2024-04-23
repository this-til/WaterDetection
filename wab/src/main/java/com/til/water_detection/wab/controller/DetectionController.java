package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Detection;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import com.til.water_detection.wab.service.DetectionService;
import com.til.water_detection.wab.util.FinalString;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detection")
@Validated
public class DetectionController {

    @Autowired
    private DetectionService detectionService;

    /***
     * 添加用户自定义的数据 成功时需要重新拉取所有数据类型
     */
    @PostMapping("/register")
    public Result<Void> register(HttpServletRequest request) {
        int userId = (int) request.getAttribute(FinalString.ID);
        detectionService.addDetection(userId);
        return Result.successful(null);
    }

    @GetMapping("/getDetectionByUserId")
    public Result<List<Detection>> getDetectionByUserId(HttpServletRequest request) {
        int userId = (int) request.getAttribute(FinalString.ID);
        List<Detection> detectionList = detectionService.getDetectionPosByUserId(userId);
        return new Result<>(ResultType.SUCCESSFUL, null, detectionList);
    }

    @GetMapping("/getDetectionById")
    public Result<Detection> getDetectionById(HttpServletRequest request, int id) {
        int userId = (int) request.getAttribute(FinalString.ID);
        Detection detection = detectionService.getDetectionById(userId, id);
        return new Result<>(detection != null ? ResultType.SUCCESSFUL : ResultType.FAIL, null, detection);
    }

    @PutMapping("/updateDetectionAnotherNameById")
    public Result<Void> updateDetectionAnotherNameById(HttpServletRequest request, int id, @Param(FinalString.VERIFY_1_30) String anotherName) {
        int userId = (int) request.getAttribute(FinalString.ID);
        int i = detectionService.updateDetectionAnotherNameById(id, userId, anotherName);
        return new Result<>(i > 0 ? ResultType.SUCCESSFUL : ResultType.FAIL, i + "条数据被更改", null);
    }
}
