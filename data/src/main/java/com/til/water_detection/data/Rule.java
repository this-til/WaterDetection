package com.til.water_detection.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rule {

    private int id;
    private int datatypeId;
    private int equipmentId;

    private float exceptionUpper;
    private float warnUpper;
    private float warnLower;
    private float exceptionLower;

    private boolean warnSendMessage;
    private boolean exceptionSendMessage;

}
