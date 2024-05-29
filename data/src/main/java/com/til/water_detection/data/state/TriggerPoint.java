package com.til.water_detection.data.state;

import lombok.Getter;

@Getter
public enum TriggerPoint {

    CYCLE(0),

    ANY_EXCEPTION(1),
    ANY_WARN(2),
    ANY_NO_NORMAL(3),
    ANY_NORMAL(4),

    ON_EXCEPTION(11),
    ON_WARN(12),
    ON_NO_NORMAL(13),
    ON_NORMAL(14),

    CUSTOM_SCOPE(20);


    private final int value;


    TriggerPoint(int value) {
        this.value = value;
    }
}
