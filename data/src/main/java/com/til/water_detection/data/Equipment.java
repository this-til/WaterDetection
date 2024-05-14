package com.til.water_detection.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equipment {
    private int id;
    private String name;
    private float longitude;
    private float latitude;
    private Timestamp upTime;

    private boolean electronicFence;
    private float fenceLongitude;
    private float fenceLatitude;
}
