package com.til.water_detection.script;

//表示当前值的状态
public enum DataState {
    EXCEPTION_UPPER,//过高异常
    WARN_UPPER,//过高警告
    NORMAL,//正常
    WARN_LOWER,//过低警告
    EXCEPTION_LOWER;//过低异常
}
