package com.til.water_detection.data;

import lombok.Getter;

@Getter
public enum ReturnState {

    SUCCESSFUL((byte) 0x01),
    FAIL((byte) 0x02),
    EXCEPTION((byte) 0x03);


    private final byte state;


    ReturnState(byte state) {
        this.state = state;
    }
}
