package com.til.water_detection.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Objects;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {

    private long id;
    private int equipmentId;
    private int dataTypeId;
    private Timestamp time;
    private float value;


}
