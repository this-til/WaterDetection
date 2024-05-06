package com.til.water_detection.data;

import java.util.ArrayList;
import java.util.List;

public enum CommandTrigger {
    ON_EXCEPTION(1),
    ON_WARN(1 << 2),
    NORMAL(1 << 3),
    HIGH(1 << 4),
    LOW(1 << 5),
    TRIGGER(1 << 6);

    private final int at;

    CommandTrigger(int at) {
        this.at = at;
    }

    public int getAt() {
        return at;
    }

    public static List<CommandTrigger> of(int tag) {
        List<CommandTrigger> list = new ArrayList<CommandTrigger>();

        for (CommandTrigger trigger : CommandTrigger.values()) {
            if ((tag & trigger.getAt()) > 0) {
                list.add(trigger);
            }
        }

        return list;
    }

}
