package com.til.water_detection.wab.socket_data;

import com.til.water_detection.data.ReturnState;

import java.util.Arrays;

public class ReturnPackage {

    private final ReturnState returnState;

    private final String[] information;

    public ReturnPackage(ReturnState returnState, String... information) {
        this.returnState = returnState;
        this.information = information;
    }

    @Override
    public String toString() {
        return ">"+ returnState.toString() + " " + String.join(" ", information);
    }
}
