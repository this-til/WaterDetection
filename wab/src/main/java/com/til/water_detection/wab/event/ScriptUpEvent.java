package com.til.water_detection.wab.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class ScriptUpEvent extends ApplicationEvent {

    public final int scriptId;
    public final String script;

    public ScriptUpEvent(Object source, int scriptId, String script) {
        super(source);
        this.scriptId = scriptId;
        this.script = script;
    }

    public ScriptUpEvent(Object source, Clock clock, int scriptId, String script) {
        super(source, clock);
        this.scriptId = scriptId;
        this.script = script;
    }
}
