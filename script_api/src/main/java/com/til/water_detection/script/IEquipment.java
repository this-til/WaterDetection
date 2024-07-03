package com.til.water_detection.script;

import org.jetbrains.annotations.Nullable;

public interface IEquipment {
    @Nullable
    IActuator getActuator(String name);
    @Nullable
    IDataType getDataType(String name);
    Iterable<IActuator> getActuators();
    Iterable<IDataType> getDataTypes();
    boolean haveUpdate();
}
