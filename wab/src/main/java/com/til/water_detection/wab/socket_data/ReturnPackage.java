package com.til.water_detection.wab.socket_data;

import com.til.water_detection.data.ReturnState;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class ReturnPackage {


    private final ReturnState returnState;
    private final byte[] information;


    public ReturnPackage(ReturnState returnState, byte[] information) {
        this.returnState = returnState;
        this.information = information;
    }


}
