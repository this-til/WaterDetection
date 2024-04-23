package com.til.water_detection.data;

import java.sql.Time;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return id == data.id && userId == data.userId && detectionId == data.detectionId && dataTypeId == data.dataTypeId && Float.compare(value, data.value) == 0 && Objects.equals(time, data.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, detectionId, dataTypeId, time, value);
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", userId=" + userId +
                ", detectionId=" + detectionId +
                ", dataTypeId=" + dataTypeId +
                ", time=" + time +
                ", value=" + value +
                '}';
    }
}
