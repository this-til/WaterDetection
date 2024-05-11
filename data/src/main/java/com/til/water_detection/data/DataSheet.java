package com.til.water_detection.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSheet {

    private DataType dataType;

    private int timeStep;
    private Timestamp startTime;
    private Timestamp endTime;

    private List<Equipment> equipmentList;
    private List<Timestamp> timestampList;

    // Equipment -> Timestamp
    private List<List<Float>> value;
}
