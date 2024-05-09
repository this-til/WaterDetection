package com.til.water_detection.data;

import java.sql.Timestamp;
import java.util.Objects;

public class Equipment {
    private int id;
    private String name;
    private float longitude;
    private float latitude;
    private Timestamp upTime;

    public Equipment() {

    }

    public Equipment(int id, String name, float longitude, float latitude, Timestamp upTime) {
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Timestamp getUpTime() {
        return upTime;
    }

    public void setUpTime(Timestamp upTime) {
        this.upTime = upTime;
    }

    @Override
    public String toString() {
        return "Detection{" +
                "id=" + id +
                ", anotherName='" + name + '\'' +
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
        return id == equipment.id && Float.compare(longitude, equipment.longitude) == 0 && Float.compare(latitude, equipment.latitude) == 0 && Objects.equals(name, equipment.name) && Objects.equals(upTime, equipment.upTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, longitude, latitude, upTime);
    }
}
