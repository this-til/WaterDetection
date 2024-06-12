package com.til.water_detection.wab.service;

import org.apache.ibatis.annotations.*;

public interface IScriptService {

    int registerScript(String script);

    int removeScript(int id);

    int updateScriptById(int id, String script);

    String getScriptById(int id);

}
