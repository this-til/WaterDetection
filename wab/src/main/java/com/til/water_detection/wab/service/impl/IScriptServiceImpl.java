package com.til.water_detection.wab.service.impl;

import com.til.water_detection.sql.mapper.IScriptMapper;
import com.til.water_detection.wab.service.IScriptService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class IScriptServiceImpl implements IScriptService {
    @Resource
    private IScriptMapper scriptMapper;

    @Override
    public int registerScript(String script) {
        return scriptMapper.registerScript(script);
    }

    @Override
    public int removeScript(int id) {
        return scriptMapper.removeScript(id);
    }

    @Override
    public int updateScriptById(int id, String script) {
        if (script.isEmpty()) {
            script = " ";
        }
        return scriptMapper.updateScriptById(id, script);
    }

    @Override
    public String getScriptById(int id) {
        return scriptMapper.getScriptById(id);
    }
}
