package com.til.water_detection.script;

//表示一种数据
public interface IDataType {
    //返回当前上报的最新值
    float getValue();
    //返回当前的状态
    DataState getDataState();
    //设置当前的状态
    void setDataState(DataState dataState);
}
