package com.til.water_detection.data;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class DataType {

    private int id;
    private String anotherName;


    public DataType(int id,  String anotherName) {
        this.id = id;
        this.anotherName = anotherName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getAnotherName() {
        return anotherName;
    }

    public void setAnotherName(String anotherName) {
        this.anotherName = anotherName;
    }

    @Override
    public String toString() {
        return "DataType{" +
                "id=" + id +
                ", anotherName='" + anotherName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataType dataType = (DataType) o;
        return id == dataType.id && Objects.equals(anotherName, dataType.anotherName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, anotherName);
    }
}
