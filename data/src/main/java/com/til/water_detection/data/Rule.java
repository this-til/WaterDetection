package com.til.water_detection.data;

import com.til.water_detection.data.state.DataState;
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

    public DataState ofDataState(float v) {
        if (v > exceptionUpper) {
            return DataState.EXCEPTION_UPPER;
        }
        if (v < exceptionLower) {
            return DataState.EXCEPTION_LOWER;
        }
        if (v < warnUpper) {
            return DataState.WARN_UPPER;
        }
        if (v < warnLower) {
            return DataState.WARN_LOWER;
        }
        return DataState.NORMAL;
    }

}
