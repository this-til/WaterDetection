package com.til.water_detection.wab.service.impl;

import com.til.water_detection.data.Detection;
import com.til.water_detection.sql.mapper.DetectionMapper;
import com.til.water_detection.wab.service.IDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetectionServiceImpl implements IDetectionService {

    @Autowired
    private DetectionMapper detectionMapper;

    @Override
    public void addDetection() {
        detectionMapper.addDetection();
    }

    @Override
    public List<Detection> getDetection() {
        return detectionMapper.getAllDetection();
    }

    @Override
    public Detection getDetectionById(int id) {
        return detectionMapper.getDetectionPosById(id);
    }

    @Override
    public int updateDetectionAnotherNameById(int id , String anotherName) {
        return detectionMapper.updateDetectionAnotherName(id, anotherName);
    }

    @Override
    public int updateDetectionTimeById(int id) {
        return detectionMapper.updateDetectionTimeById(id);
    }
}
