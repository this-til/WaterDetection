package com.til.water_detection.data.util;

import java.nio.ByteBuffer;

public class FinalByte {

    public static final byte ORDER = 0x01;
    public static final byte ANSWER_BACK = 0x02;

    public static final byte URL = 0x01;
    public static final byte USERNAME = 0x02;
    public static final byte PASSWORD = 0x03;
    public static final byte EQUIPMENT = 0x04;
    public static final byte RULE = 0x05;

    public static final byte DATA_TYPE = 0x01;
    public static final byte ACTUATOR = 0x02;
    public static final byte DATA_TYPE_LIST = 0x03;
    public static final byte ACTUATOR_LIST = 0x04;

    public static final byte WRITE = 0x01;
    public static final byte READ = 0x02;
    public static final byte GET = 0x03;
    public static final byte INIT_END = 0x04;
    public static final byte SYNC_END = 0x05;

    public static final byte SUCCESSFUL = 0x01;
    public static final byte FAIL = 0x02;
    public static final byte EXCEPTION = 0x03;

    public static final byte SERVER = 0x01;
    public static final byte CLIENT = 0x02;
    public static final byte SCREEN = 0x03;
    public static final byte _4G = 0x04;


    // 服务端
    public static final byte S_WRITE = 0x01;
    public static final byte S_REPORTING = 0x02;
    public static final byte S_TIME = 0x03;

    public static final byte S_RULE = 0x01;
    public static final byte S_EQUIPMENT_NAME = 0x02;

    public static final byte S_DATA = 0x01;
    public static final byte S_GPS = 0x02;

    public static final byte[] FRAME_HEADER = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA};
    public static final byte[] FRAME_FOOTER = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    //public static final byte S_EQUIPMENT_NAME_SUCCESSFUL = 0x01;
    //public static final byte S_EQUIPMENT_NAME_DUPLICATE_NAME = 0x02;

    public static ByteBuffer extractFrame(ByteBuffer buffer, byte[] FRAME_HEADER, byte[] FRAME_FOOTER) {
        while (buffer.remaining() >= FRAME_HEADER.length) {
            // 检查是否找到帧头
            int headerPosition = buffer.position();
            for (byte b : FRAME_HEADER) {
                if (buffer.get(buffer.position()) != b) {
                    buffer.position(headerPosition + 1); // 移动到下一个字节并继续搜索
                    break;
                }
                buffer.position(buffer.position() + 1);
            }

            // 如果找到帧头，则尝试提取帧内容
            if (buffer.remaining() >= FRAME_HEADER.length + FRAME_FOOTER.length) {
                ByteBuffer frameBuffer = ByteBuffer.allocate(buffer.remaining() - FRAME_FOOTER.length);
                frameBuffer.put(buffer.array(), buffer.position(), buffer.remaining() - FRAME_FOOTER.length);
                frameBuffer.flip();

                // 搜索帧尾
                boolean foundFooter = false;
                for (int i = 0; i < FRAME_FOOTER.length; i++) {
                    if (frameBuffer.get(frameBuffer.limit() - FRAME_FOOTER.length + i) != FRAME_FOOTER[i]) {
                        foundFooter = false;
                        break;
                    }
                    foundFooter = true;
                }

                // 如果找到帧尾，则截断缓冲区并返回它
                if (foundFooter) {
                    frameBuffer.limit(frameBuffer.limit() - FRAME_FOOTER.length);
                    buffer.position(buffer.position() + frameBuffer.limit() + FRAME_FOOTER.length); // 移动到下一个帧的开始
                    return frameBuffer;
                }
            }
        }
        // 如果没有找到帧，则返回null或抛出异常
        return null;
    }

}
