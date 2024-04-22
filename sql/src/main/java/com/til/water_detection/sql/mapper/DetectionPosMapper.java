package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.Detection;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DetectionPosMapper {

    int addDetectionPos(Detection detectionPos);

    int removeDetectionPosById(int id);

    int updateDetectionPosAnotherNameById(@Param("id") int id, @Param("anotherName") String anotherName);

    int updateDetectionPosTimeById(int id);

    int updateDetectionPosCoordinate(@Param("id") int id, @Param("longitude") float longitude, @Param("latitude") float latitude);

    Detection getDetectionPosById(int id);

    List<Detection> getDetectionListByUserId(int userId);
}
