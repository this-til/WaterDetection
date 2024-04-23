package com.til.water_detection.data;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class DataType {

    private int id;
    private int userId;
    @Nullable
    private String anotherName;


    public DataType(int id, int userId, @Nullable String anotherName) {
        this.id = id;
        this.userId = userId;
        this.anotherName = anotherName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataType dataType = (DataType) o;
        return id == dataType.id && userId == dataType.userId && Objects.equals(anotherName, dataType.anotherName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, anotherName);
    }

    @Override
    public String toString() {
        return "DataType{" +
                "id=" + id +
                ", userID=" + userId +
                ", anotherName='" + anotherName + '\'' +
                '}';
    }
}
