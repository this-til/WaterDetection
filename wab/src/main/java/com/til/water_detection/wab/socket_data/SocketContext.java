package com.til.water_detection.wab.socket_data;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.socket.WebSocketSession;

import java.util.LinkedList;
import java.util.Queue;

public class SocketContext<C extends CommandCallback< ?>> {
    @Getter
    protected final WebSocketSession webSocketSession;
    private final Queue<C> commandCallbacks = new LinkedList<>();
    private C activityCommandCallbacks;
    @Getter
    private long activityCommandStartTime;
    @Getter
    private long finalUpdate = System.currentTimeMillis();

    public SocketContext(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public void update() {
        finalUpdate = System.currentTimeMillis();
    }

    @Nullable
    public C nextCommand() {
        activityCommandCallbacks = commandCallbacks.poll();
        activityCommandStartTime = System.currentTimeMillis();
        return activityCommandCallbacks;
    }

    public C completeSession() {
        C _activityCommandCallbacks = activityCommandCallbacks;
        return _activityCommandCallbacks;
    }

    public boolean haveActivityCommandCallbacks() {
        return activityCommandCallbacks != null;
    }

    public void addCommandCallback(C commandCallback) {
        commandCallbacks.add(commandCallback);
    }
}
