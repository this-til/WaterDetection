package com.til.water_detection.wab.socket_data;

import com.til.water_detection.data.Tag;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.ByteBuffer;

@Data
@AllArgsConstructor
public class ComEntry<C> {

    private byte sign;
    private IDecode<C> decode;

    public interface IDecode<C> {
        void decode(ByteBuf source, ByteBuf output, Tag tag, C c);
    }

}
