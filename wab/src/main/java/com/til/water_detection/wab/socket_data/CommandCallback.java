package com.til.water_detection.wab.socket_data;

import com.til.water_detection.data.util.FinalByte;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.EmptyByteBuf;
import lombok.Getter;
import org.apache.ibatis.javassist.bytecode.ByteArray;
import org.springframework.core.codec.ByteBufferEncoder;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

@Getter
public class CommandCallback<S extends SocketContext<?>> {

    public final byte[] command;
    public final int cId;

    public boolean send;
    public long sendTime;

    private static int id;

    public CommandCallback(byte[] command) {

        cId = id++;

        this.command = new byte[command.length + 9];
        System.arraycopy(command, 6, this.command, 0, command.length);
        this.command[0] = FinalByte.SERVER; // 来源
        this.command[1] = FinalByte.ORDER; // 头

        ByteArray.write32bit(cId, this.command, 2);

        this.command[this.command.length - 1] = (byte) 0xff;
        this.command[this.command.length - 2] = (byte) 0xff;
        this.command[this.command.length - 3] = (byte) 0xff;


    }

    public void send() {
        send = true;
        sendTime = System.currentTimeMillis();
    }

    public void successCallback(ByteBuf byteBuf, S socketContext) throws IOException {
    }

    public void failCallback(ByteBuf byteBuf, S socketContext)  throws IOException{
    }

    public void exceptionCallback(ByteBuf byteBuf, S socketContext) throws IOException {
    }

    public void outTime(S socketContext) {
    }

}
