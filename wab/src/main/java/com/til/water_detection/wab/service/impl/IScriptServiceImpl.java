package com.til.water_detection.wab.service.impl;

import com.til.water_detection.sql.mapper.IScriptMapper;
import com.til.water_detection.wab.event.ScriptUpEvent;
import com.til.water_detection.wab.service.IScriptService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class IScriptServiceImpl implements IScriptService {
    @Resource
    private IScriptMapper scriptMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

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
        int i = scriptMapper.updateScriptById(id, script);
        applicationEventPublisher.publishEvent(new ScriptUpEvent(this, id, script));
        return i;
    }

    @Override
    public String getScriptById(int id) {
        return scriptMapper.getScriptById(id);
    }
}
