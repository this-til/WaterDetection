package com.til.water_detection.script;

//表示一个执行器
public interface IActuator {
    //执行器是否在运行
    boolean isRun();
    //运行
    void start();
    //停止
    void stop();
}
