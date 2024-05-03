package com.til.water_detection.data;

import java.sql.Timestamp;

public class DataFilter {
    private int dataTypeId;
    private int[] equipmentIdArray;
    private int timeStep;
    private Timestamp startTime;
    private Timestamp endTime;

    public DataFilter() {
    }

    public DataFilter(int dataTypeId, int[] equipmentIdArray, int timeStep, Timestamp startTime, Timestamp endTime) {
        this.dataTypeId = dataTypeId;
        this.equipmentIdArray = equipmentIdArray;
        this.timeStep = timeStep;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(int dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public int[] getEquipmentIdArray() {
        return equipmentIdArray;
    }

    public void setEquipmentIdArray(int[] equipmentIdArray) {
        this.equipmentIdArray = equipmentIdArray;
    }

    public int getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
