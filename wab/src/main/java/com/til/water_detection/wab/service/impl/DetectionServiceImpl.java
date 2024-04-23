package com.til.water_detection.wab.service.impl;

import com.til.water_detection.data.Detection;
import com.til.water_detection.sql.mapper.DetectionMapper;
import com.til.water_detection.wab.service.DetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetectionServiceImpl implements DetectionService {

    @Autowired
    private DetectionMapper detectionMapper;

    @Override
    public void addDetection(int userId) {
        detectionMapper.addDetection(userId);
    }

    @Override
    public List<Detection> getDetectionPosByUserId(int userId) {
        return detectionMapper.getDetectionListByUserId(userId);
    }

    @Override
    public Detection getDetectionById(int id, int userId) {
        return detectionMapper.getDetectionPosById(id, userId);
    }

    @Override
    public void setDetectionAnotherNameById(int id, int userId, String anotherName) {
        detectionMapper.updateDetectionPosAnotherNameById(id, userId, anotherName);
    }

    @Override
    public void updateDetectionPosTimeById(int id, int userId) {
        detectionMapper.updateDetectionPosTimeById(id, userId);
    }
}
