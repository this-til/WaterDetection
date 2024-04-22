package com.til.water_detection.data;

import java.sql.Time;

public final class Detection {
    private int id;
    private int userId;
    private String anotherName;
    private float longitude;
    private float latitude;
    private Time upTime;

    public Detection(int id, int userId, String anotherName, float longitude, float latitude, Time upTime) {
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

    public String getAnotherName() {
        return anotherName;
    }

    public void setAnotherName(String anotherName) {
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
}
