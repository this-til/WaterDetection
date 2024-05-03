package com.til.water_detection.data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class DataSheet {

    private DataType dataType;

    private int timeStep;
    private Timestamp startTime;
    private Timestamp endTime;

    private List<Equipment> equipmentList;
    private List<Timestamp> timestampList;

    // Equipment -> Timestamp
    private List<List<Float>> value;

    public DataSheet() {
    }

    public DataSheet(
            DataType dataType,
            int timeStep,
            Timestamp startTime,
            Timestamp endTime,
            List<Equipment> equipmentList,
            List<Timestamp> timestampList,
            List<List<Float>> value
    ) {
        this.dataType = dataType;
        this.timeStep = timeStep;
        this.startTime = startTime;
        this.endTime = endTime;
        this.equipmentList = equipmentList;
        this.timestampList = timestampList;
        this.value = value;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
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

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public List<Timestamp> getTimestampList() {
        return timestampList;
    }

    public void setTimestampList(List<Timestamp> timestampList) {
        this.timestampList = timestampList;
    }

    public List<List<Float>> getValue() {
        return value;
    }

    public void setValue(List<List<Float>> value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSheet dataSheet = (DataSheet) o;
        return timeStep == dataSheet.timeStep && Objects.equals(dataType, dataSheet.dataType) && Objects.equals(startTime, dataSheet.startTime) && Objects.equals(endTime, dataSheet.endTime) && Objects.equals(equipmentList, dataSheet.equipmentList) && Objects.equals(timestampList, dataSheet.timestampList) && Objects.equals(value, dataSheet.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataType, timeStep, startTime, endTime, equipmentList, timestampList, value);
    }
}
