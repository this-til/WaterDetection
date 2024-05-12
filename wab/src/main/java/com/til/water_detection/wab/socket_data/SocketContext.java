package com.til.water_detection.wab.socket_data;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Getter
public class SocketContext<C extends CommandCallback<?>> {
    protected final WebSocketSession webSocketSession;
    private final LinkedList<C> commandCallbacks = new LinkedList<>();
    private long finalUpdate = System.currentTimeMillis();

    public SocketContext(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public void update() {
        finalUpdate = System.currentTimeMillis();
    }

    public void addCommandCallback(C commandCallback) {
        commandCallbacks.add(commandCallback);
    }
}
