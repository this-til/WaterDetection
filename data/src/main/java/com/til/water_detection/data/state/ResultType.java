package com.til.water_detection.data.state;


import lombok.Getter;

@Getter
public enum ResultType {

    SUCCESSFUL((byte) 0x01),
    FAIL((byte) 0x02),
    ERROR((byte) 0x03);


    private final byte state;


    ResultType(byte state) {
        this.state = state;
    }
}
