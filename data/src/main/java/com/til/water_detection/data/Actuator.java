package com.til.water_detection.data;

import java.util.Objects;

public class Actuator {

    private int id;
    private String name;

    public Actuator() {
    }

    public Actuator(int id, String name) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actuator actuator = (Actuator) o;
        return id == actuator.id && Objects.equals(name, actuator.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
