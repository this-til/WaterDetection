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
    public static final byte S_DATA_LIST = 0x03;
    public static final byte S_ACTUATOR_LIST = 0x04;

    public static final byte[] FRAME_HEADER = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA};
    public static final byte[] FRAME_FOOTER = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    //public static final byte S_EQUIPMENT_NAME_SUCCESSFUL = 0x01;
    //public static final byte S_EQUIPMENT_NAME_DUPLICATE_NAME = 0x02;
    public static byte[] extractFrame(byte[] buffer, byte[] FRAME_HEADER, byte[] FRAME_FOOTER) {
        if (buffer == null || FRAME_HEADER == null || FRAME_FOOTER == null || buffer.length < FRAME_HEADER.length + FRAME_FOOTER.length) {
            return null; // 无效输入或buffer太短
        }

        int headerIndex = indexOf(buffer, FRAME_HEADER, 0);
        if (headerIndex == -1) {
            return null; // 未找到帧头
        }

        int footerIndex = indexOf(buffer, FRAME_FOOTER, headerIndex + FRAME_HEADER.length);
        if (footerIndex == -1) {
            return null; // 未找到帧尾
        }

        // 提取帧数据（不包括帧头和帧尾）
        int frameLength = footerIndex - headerIndex - FRAME_HEADER.length;
        byte[] frameData = new byte[frameLength];
        System.arraycopy(buffer, headerIndex + FRAME_HEADER.length, frameData, 0, frameLength);

        return frameData;
    }

    // 辅助方法：从buffer的指定位置开始查找子数组
    public static int indexOf(byte[] buffer, byte[] subArray, int fromIndex) {
        for (int i = fromIndex; i <= buffer.length - subArray.length; i++) {
            int j;
            for (j = 0; j < subArray.length; j++) {
                if (buffer[i + j] != subArray[j]) {
                    break;
                }
            }
            if (j == subArray.length) {
                return i; // 找到子数组
            }
        }
        return -1; // 未找到子数组
    }


}
