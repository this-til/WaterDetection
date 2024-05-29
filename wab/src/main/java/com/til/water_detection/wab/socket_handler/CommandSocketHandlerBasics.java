package com.til.water_detection.wab.socket_handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.til.water_detection.data.Tag;
import com.til.water_detection.data.state.ResultType;
import com.til.water_detection.data.util.FinalByte;
import com.til.water_detection.data.util.FinalString;
import com.til.water_detection.data.util.Util;
import com.til.water_detection.wab.socket_data.CommandCallback;
import com.til.water_detection.wab.socket_data.ReturnPackage;
import com.til.water_detection.wab.socket_data.SocketContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import jakarta.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class CommandSocketHandlerBasics<S extends SocketContext<?>> extends AbstractWebSocketHandler {
    protected final Logger logger = LogManager.getLogger(EquipmentSocketHandler.class);
    protected final Map<WebSocketSession, S> map = new ConcurrentHashMap<>();
    protected final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public CommandSocketHandlerBasics() {
        scheduler.scheduleAtFixedRate(this::sendHeartbeat, 0, 30, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::heartbeatDetection, 0, 30, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::timeoutDetection, 0, 30, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::send, 0, 1, TimeUnit.SECONDS);
    }

    protected void sendHeartbeat() {
        for (Map.Entry<WebSocketSession, S> entry : map.entrySet()) {
            try {
                entry.getKey().sendMessage(new BinaryMessage(new byte[]{0x00}));
            } catch (IOException e) {
                logger.error("心跳发送失败:：", e);
            }
        }
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
        long timeMillis = System.currentTimeMillis() - 10 * 1000;

        for (Map.Entry<WebSocketSession, S> entry : map.entrySet()) {
            for (CommandCallback<?> commandCallback : entry.getValue().getCommandCallbacks()) {
                if (!commandCallback.isSend()) {
                    continue;
                }
                if (commandCallback.getSendTime() < timeMillis) {
                    commandCallback.outTime(Util.cast(entry.getValue()));

                    if (commandCallback.getTheRemainingNumberOfResends() > 0) {
                        commandCallback.setTheRemainingNumberOfResends(commandCallback.getTheRemainingNumberOfResends() - 1);
                        commandCallback.setSend(false);
                    }
                }
            }
        }
    }

    protected void send() {
        for (Map.Entry<WebSocketSession, S> entry : map.entrySet()) {

            for (CommandCallback<?> commandCallback : entry.getValue().getCommandCallbacks()) {
                if (commandCallback.isSend()) {
                    continue;
                }
                WebSocketSession key = entry.getKey();
                synchronized (key) {
                    try {
                        key.sendMessage(new BinaryMessage(commandCallback.getCommand()));
                    } catch (IOException e) {
                        logger.error("发送指令异常：", e);
                    }
                }
                commandCallback.send();
            }
        }
    }

    protected abstract S mackSocketContext(WebSocketSession session) throws IOException;

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        S value = mackSocketContext(session);
        map.put(session, value);
        value.update();
        logger.info("新的连接 id={} remoteAddress={}", session.getId(), session.getRemoteAddress());
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        map.remove(session);
        logger.info("连接断开 id={} remoteAddress={} closeStatus={}", session.getId(), session.getRemoteAddress(), status);
    }


    @Override
    protected void handleBinaryMessage(@NotNull WebSocketSession session, @NotNull BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);
        logger.info("新的消息 id={} remoteAddress={} message={}", session.getId(), session.getRemoteAddress(), message.getPayload().array());

        S socketContext = map.get(session);
        socketContext.update();

        ByteBuffer byteBuffer = message.getPayload();

        byte[] _byteBuffer = FinalByte.extractFrame(byteBuffer.array(), FinalByte.FRAME_HEADER, FinalByte.FRAME_FOOTER);
        if (_byteBuffer == null) {
            return;
        }
        ByteBuf byteBuf = Unpooled.copiedBuffer(_byteBuffer);
        byte from = byteBuf.readByte();
        byte to = byteBuf.readByte();
        byte head = byteBuf.readByte();
        int id = byteBuf.readInt();

        Tag tag = new Tag(from, to, head, id, (byte) 0);

        if (to != FinalByte.SERVER) {
            synchronized (session) {
                session.sendMessage(new BinaryMessage(byteBuffer));
            }
        }

        switch (head) {
            case FinalByte.ORDER -> {
                ByteBuf buf = Unpooled.buffer();
                buf.writeBytes(FinalByte.FRAME_HEADER);
                buf.writeByte(from);
                buf.writeByte(FinalByte.ANSWER_BACK);
                buf.writeInt(id);
                try {
                    command(byteBuf, buf, tag, socketContext);
                } catch (Exception e) {
                    logger.error(e);

                    buf = Unpooled.buffer();
                    buf.writeByte(0xaa);
                    buf.writeByte(0xaa);
                    buf.writeByte(0xaa);
                    buf.writeByte(to);
                    buf.writeByte(from);
                    buf.writeByte(FinalByte.ANSWER_BACK);
                    buf.writeInt(id);
                    buf.writeByte(ResultType.ERROR.getState());
                    buf.writeBytes(e.getMessage().getBytes(StandardCharsets.UTF_8));
                }
                buf.writeBytes(FinalByte.FRAME_FOOTER);
                session.sendMessage(new BinaryMessage(buf.array()));
            }
            case FinalByte.ANSWER_BACK -> {

                byte answerState = byteBuf.readByte();
                tag.setAnswerState(answerState);

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
                    return;
                }

                switch (answerState) {
                    case FinalByte.SUCCESSFUL -> commandCallback.successCallback(byteBuf, Util.cast(socketContext));
                    case FinalByte.FAIL -> commandCallback.failCallback(byteBuf, Util.cast(socketContext));
                    case FinalByte.EXCEPTION -> commandCallback.exceptionCallback(byteBuf, Util.cast(socketContext));
                    default -> {
                        return;
                    }
                }

                socketContext.getCommandCallbacks().remove(commandCallback);
            }
        }

    }

/*    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        logger.info("新的消息 id={} remoteAddress={} message={}", session.getId(), session.getRemoteAddress(), message.getPayload());
        map.get(session).update();
        session.sendMessage(new TextMessage(message.getPayload()));
    }*/

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

    protected abstract void command(ByteBuf source, ByteBuf output, Tag tag, S s);

    public Collection<S> getSocketContext() {
        return map.values();
    }
}
