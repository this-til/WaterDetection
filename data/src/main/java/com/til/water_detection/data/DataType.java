package com.til.water_detection.data;

import java.util.Objects;

public class DataType {

    private int id;
    private String name;

    public DataType() {
    }

    public DataType(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "DataType{" +
                "id=" + id +
                ", anotherName='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataType dataType = (DataType) o;
        return id == dataType.id && Objects.equals(name, dataType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
