package com.til.water_detection.data;

import java.sql.Time;

public final class Data {

    private long id;
    private int userId;
    private int detectionId;
    private int dataTypeId;
    private Time time;
    private float value;

    public Data(long id, int userId, int detectionId, int dataTypeId, Time time, float value) {
        this.id = id;
        this.userId = userId;
        this.detectionId = detectionId;
        this.dataTypeId = dataTypeId;
        this.time = time;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDetectionId() {
        return detectionId;
    }

    public void setDetectionId(int detectionId) {
        this.detectionId = detectionId;
    }

    public int getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(int dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
