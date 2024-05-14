package com.til.water_detection.wab.socket_handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class TestHandler extends AbstractWebSocketHandler {
    protected final Logger logger = LogManager.getLogger(EquipmentSocketHandler.class);

    protected final List<WebSocketSession> listeners = new ArrayList<>();

    protected final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public TestHandler() {
        scheduler.scheduleAtFixedRate(() -> {

            for (WebSocketSession listener : listeners) {
                synchronized (listener) {
                    byte[] bytes = new byte[]{0x01, 0x02, 0x03, 0x04};
                    try {
                        //listener.sendMessage(new TextMessage(Base64.getEncoder().encodeToString(bytes)));
                        listener.sendMessage(new BinaryMessage(bytes));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        listeners.add(session);
        logger.info("新的连接 id={} remoteAddress={}", session.getId(), session.getRemoteAddress());
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        listeners.remove(session);
        logger.info("连接断开 id={} remoteAddress={} closeStatus={}", session.getId(), session.getRemoteAddress(), status);
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
       /* synchronized (session) {
            session.sendMessage(new TextMessage(Base64.getEncoder().encodeToString(message.getPayload().getBytes())));
        }*/
        logger.info("新的数据 id={} data={}", session.getId(), message.getPayload());
    }


    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);
        logger.info("新的数据 id={} data={}", session.getId(), message.getPayload().array());
    }
}
