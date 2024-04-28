package com.til.water_detection.data;

import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.util.Objects;

public final class Equipment {
    private int id;
    @Nullable
    private String anotherName;
    private float longitude;
    private float latitude;
    private Time upTime;

    public Equipment(int id, @Nullable String anotherName, float longitude, float latitude, Time upTime) {
        this.id = id;
        this.anotherName = anotherName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.upTime = upTime;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    public String getAnotherName() {
        return anotherName;
    }

    public void setAnotherName(@Nullable String anotherName) {
        this.anotherName = anotherName;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public Time getUpTime() {
        return upTime;
    }

    public void setUpTime(Time upTime) {
        this.upTime = upTime;
    }

    @Override
    public String toString() {
        return "Detection{" +
                "id=" + id +
                ", anotherName='" + anotherName + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", upTime=" + upTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return id == equipment.id && Float.compare(longitude, equipment.longitude) == 0 && Float.compare(latitude, equipment.latitude) == 0 && Objects.equals(anotherName, equipment.anotherName) && Objects.equals(upTime, equipment.upTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, anotherName, longitude, latitude, upTime);
    }
}
