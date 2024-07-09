package com.til.water_detection.script;

import org.jetbrains.annotations.Nullable;

//表示当前设备
public interface IEquipment {
    //根据名称获得执行器，如果为null表示没有对应的执行器
    @Nullable
    IActuator getActuator(String name);
    //根据名称获得传感器，如果为null表示没有对应的传感器
    @Nullable
    IDataType getDataType(String name);
    //返回所有的执行器
    Iterable<IActuator> getActuators();
    //返回所有的传感器
    Iterable<IDataType> getDataTypes();
    //表示距离上一次调用中间是否有数据更新，调用方法一次后将永远返回false，知道下次数据上报
    boolean haveUpdate();
}
