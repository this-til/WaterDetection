package com.til.water_detection.data.util;

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

    // 服务端
    public static final byte WRITE_RULE = 0x01;
    public static final byte REPORTING = 0x02;
    public static final byte TIME = 0x03;

    public static final byte SUCCESSFUL = 0x01;
    public static final byte FAIL = 0x02;
    public static final byte EXCEPTION = 0x03;

    public static final byte SERVER = 0x01;
    public static final byte CLIENT = 0x02;

}
