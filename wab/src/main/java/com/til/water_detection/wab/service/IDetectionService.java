package com.til.water_detection.wab.service;

import com.til.water_detection.data.Detection;
import jakarta.annotation.Nullable;

import java.util.List;

public interface IDetectionService {
    void addDetection();

    List<Detection> getDetection();

    @Nullable
    Detection getDetectionById(int id);

    int updateDetectionAnotherNameById(int id, String anotherName);

    int updateDetectionTimeById(int id);
}
