package com.til.water_detection.wab.socket_data;

import com.til.water_detection.data.util.FinalByte;
import com.til.water_detection.data.util.FinalString;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.javassist.bytecode.ByteArray;
import org.springframework.core.codec.ByteBufferEncoder;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

@Getter
public class CommandCallback<S extends SocketContext<?>> {

    public final byte[] command;
    public final int cId;

    @Setter
    public boolean send;
    public long sendTime;

    @Setter
    public int theRemainingNumberOfResends ;

    private static int id;

    public CommandCallback(ByteBuf command) {

        cId = id++;

        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(0xAA);
        buffer.writeByte(0xAA);
        buffer.writeByte(0xAA);
        buffer.writeByte(FinalByte.SERVER);
        buffer.writeByte(FinalByte.CLIENT);
        buffer.writeByte(FinalByte.ORDER);
        buffer.writeInt(cId);
        if (command != null) {
            buffer.writeBytes(command);
        }
        buffer.writeByte(0xFF);
        buffer.writeByte(0xFF);
        buffer.writeByte(0xFF);
        this.command = buffer.array();


    }

    public void send() {
        send = true;
        sendTime = System.currentTimeMillis();
    }

    public void successCallback(ByteBuf byteBuf, S socketContext) throws IOException {
    }

    public void failCallback(ByteBuf byteBuf, S socketContext) throws IOException {
    }

    public void exceptionCallback(ByteBuf byteBuf, S socketContext) throws IOException {
    }

    public void outTime(S socketContext) {
    }

}
