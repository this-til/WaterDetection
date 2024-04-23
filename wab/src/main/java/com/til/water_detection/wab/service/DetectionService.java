package com.til.water_detection.wab.service;

import com.til.water_detection.data.Detection;
import jakarta.annotation.Nullable;

import java.util.List;

public interface DetectionService {
    void addDetection(int userId);

    List<Detection> getDetectionPosByUserId(int userId);

    @Nullable
    Detection getDetectionById(int id, int userId);

    int updateDetectionAnotherNameById(int id, int userId, String anotherName);

    int updateDetectionTimeById(int id, int userId);
}
