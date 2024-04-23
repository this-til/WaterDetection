package com.til.water_detection.data;

import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.util.Objects;

public final class Detection {
    private int id;
    private int userId;
    @Nullable
    private String anotherName;
    private float longitude;
    private float latitude;
    private Time upTime;

    public Detection(int id, int userId, @Nullable String anotherName, float longitude, float latitude, Time upTime) {
        this.id = id;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
                ", userId=" + userId +
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
        Detection detection = (Detection) o;
        return id == detection.id && userId == detection.userId && Float.compare(longitude, detection.longitude) == 0 && Float.compare(latitude, detection.latitude) == 0 && Objects.equals(anotherName, detection.anotherName) && Objects.equals(upTime, detection.upTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, anotherName, longitude, latitude, upTime);
    }
}
