package com.til.water_detection.wab.socket_handler;

import com.til.water_detection.data.DataType;
import com.til.water_detection.data.ReturnState;
import com.til.water_detection.data.util.FinalByte;
import com.til.water_detection.data.util.FinalString;
import com.til.water_detection.data.util.Util;
import com.til.water_detection.wab.socket_data.CommandCallback;
import com.til.water_detection.wab.socket_data.EquipmentSocketContext;
import com.til.water_detection.wab.socket_data.ReturnPackage;
import com.til.water_detection.wab.socket_data.SocketContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class CommandSocketHandlerBasics<S extends SocketContext<?>> extends BinaryWebSocketHandler {
    protected final Logger logger = LogManager.getLogger(EquipmentSocketHandler.class);
    protected final Map<WebSocketSession, S> map = new ConcurrentHashMap<>();
    protected final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public CommandSocketHandlerBasics() {
        scheduler.scheduleAtFixedRate(this::heartbeatDetection, 0, 30, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::timeoutDetection, 0, 30, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::send, 0, 1, TimeUnit.SECONDS);
    }

    protected void heartbeatDetection() {
        long timeMillis = System.currentTimeMillis() - 60 * 1000;

        for (Map.Entry<WebSocketSession, S> entry : map.entrySet()) {
            if (entry.getValue().getFinalUpdate() < timeMillis) {
                try {
                    entry.getKey().close(CloseStatus.SESSION_NOT_RELIABLE);
                } catch (IOException e) {
                    logger.error("心跳超时关闭时发生异常：", e);
                }
            }
        }
    }

    protected void timeoutDetection() {
        long timeMillis = System.currentTimeMillis() - 60 * 1000;

        for (Map.Entry<WebSocketSession, S> entry : map.entrySet()) {
            for (CommandCallback<?> commandCallback : entry.getValue().getCommandCallbacks()) {
                if (!commandCallback.isSend()) {
                    continue;
                }
                if (commandCallback.getSendTime() < timeMillis) {
                    try {
                        entry.getKey().close(CloseStatus.SESSION_NOT_RELIABLE);
                    } catch (IOException e) {
                        logger.error("任务超时关闭时发生异常：", e);
                    }
                }
            }
        }
    }

    protected void send() {
        for (Map.Entry<WebSocketSession, S> entry : map.entrySet()) {

            for (CommandCallback<?> commandCallback : entry.getValue().getCommandCallbacks()) {
                if (commandCallback.isSend()) {
                    return;
                }
                WebSocketSession key = entry.getKey();
                synchronized (key) {
                    try {
                        key.sendMessage(new BinaryMessage(commandCallback.getCommand()));
                    } catch (IOException e) {
                        logger.error("发送指令时异常：", e);
                    }
                }
            }
        }
    }

    protected abstract S mackSocketContext(WebSocketSession session);

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        map.put(session, mackSocketContext(session));
        logger.info("新的连接 id={} url={}", session.getId(), session.getUri());
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        map.remove(session);
        logger.info("连接断开 id={} url={} closeStatus={}", session.getId(), session.getUri(), status);
    }


    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);
        logger.info("新的消息 id={} url={} message={}", session.getId(), session.getUri(), message.getPayload());


        S socketContext = map.get(session);
        socketContext.update();

        ByteBuffer byteBuffer = message.getPayload();
        ByteBuf byteBuf = Unpooled.copiedBuffer(byteBuffer);

        byte source = byteBuf.readByte(); // 不使用
        byte head = byteBuf.readByte();
        int id = byteBuf.readInt();

        switch (head) {
            case FinalByte.ORDER -> {
                ReturnPackage command = command(byteBuf, socketContext);
                ByteBuf buf = Unpooled.buffer();
                buf.writeByte(FinalByte.SERVER);
                buf.writeByte(FinalByte.ANSWER_BACK);
                buf.writeInt(id);
                buf.writeByte(command.getReturnState().getState());
                buf.writeBytes(command.getInformation());
                buf.writeByte(0xff).writeByte(0xff).writeByte(0xff);
            }
            case FinalByte.ANSWER_BACK -> {

                byte answerState = byteBuf.readByte();

                CommandCallback<?> commandCallback = null;

                for (CommandCallback<?> _commandCallback : socketContext.getCommandCallbacks()) {
                    if (!_commandCallback.isSend()) {
                        continue;
                    }
                    if (_commandCallback.cId == id) {
                        commandCallback = _commandCallback;
                        break;
                    }
                }

                if (commandCallback == null) {
                    session.close(CloseStatus.NOT_ACCEPTABLE.withReason("未知的应答:" + id));
                    return;
                }

                switch (answerState) {
                    case FinalByte.SUCCESSFUL -> commandCallback.successCallback(byteBuf, Util.cast(socketContext));
                    case FinalByte.FAIL -> {
                        commandCallback.failCallback(byteBuf, Util.cast(socketContext));
                        session.close(CloseStatus.NOT_ACCEPTABLE);
                    }
                    case FinalByte.EXCEPTION -> {
                        commandCallback.exceptionCallback(byteBuf, Util.cast(socketContext));
                        session.close(CloseStatus.NOT_ACCEPTABLE);
                    }
                    default -> session.close(CloseStatus.NOT_ACCEPTABLE.withReason("未定状态:" + answerState));
                }
            }
            default -> session.close(CloseStatus.NOT_ACCEPTABLE.withReason("未定义头:" + head));
        }

    }

    /*@Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) throws Exception {
        logger.info("新的消息 id={} url={} message={}", session.getId(), session.getUri(), message.getPayload());

        S equipmentSocketContext = map.get(session);

        String payload = message.getPayload().trim();

        char head = payload.charAt(0);

        equipmentSocketContext.update();

        if (head == '~') {
            //仅完成心跳
            return;
        }

        if (head != '/' && head != '>') {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("未定义标签:" + payload));
            return;
        }

        String[] pack = Arrays.stream(payload.substring(1).split(" "))
                .filter(String::isEmpty)
                .toArray(String[]::new);

        if (pack.length == 0) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("未定义语法:" + payload));
        }
        switch (head) {
            case '/':
                ReturnPackage returnPackage;
                try {
                    returnPackage = command(pack, equipmentSocketContext);
                } catch (Exception e) {
                    returnPackage = new ReturnPackage(ReturnState.FAIL, e.getMessage());
                }
                synchronized (session) {
                    session.sendMessage(new TextMessage(returnPackage.toString()));
                }
                return;
            case '>':

                String s = pack[0];

                CommandCallback<?> commandCallback = equipmentSocketContext.completeSession();

                switch (s) {
                    case "SUCCESSFUL":
                        try {
                            commandCallback.successCallback(Arrays.copyOfRange(pack, 1, pack.length), Util.cast(equipmentSocketContext));
                        } catch (Exception e) {
                            session.close(CloseStatus.SERVER_ERROR.withReason(e.getMessage()));
                        }
                        break;
                    case "FAIL":
                        try {
                            commandCallback.failCallback(Arrays.copyOfRange(pack, 1, pack.length), Util.cast(equipmentSocketContext));
                        } catch (Exception e) {
                            session.close(CloseStatus.SERVER_ERROR.withReason(e.getMessage()));
                        }
                        break;
                    default:
                        session.close(CloseStatus.NOT_ACCEPTABLE.withReason("未定义语句:" + payload));
                }


        }
    }*/

    protected abstract ReturnPackage command(ByteBuf byteBuf, S s);

    public Collection<S> getSocketContext() {
        return map.values();
    }
}
