package com.til.water_detection.data;

import java.sql.Timestamp;
import java.util.Objects;

public final class Data {

    private long id;
    private int equipmentId;
    private int dataTypeId;
    private Timestamp time;
    private float value;

    public Data(long id, int equipmentId, int dataTypeId, Timestamp time, float value) {
        this.id = id;
        this.equipmentId = equipmentId;
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


    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(int dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
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
        return id == data.id && equipmentId == data.equipmentId && dataTypeId == data.dataTypeId && Float.compare(value, data.value) == 0 && Objects.equals(time, data.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, equipmentId, dataTypeId, time, value);
    }
}
