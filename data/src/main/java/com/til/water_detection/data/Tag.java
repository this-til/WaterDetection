package com.til.water_detection.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tag {
    private byte from;
    private byte to;
    private byte header;
    private int id;

    // 仅应答
    private byte answerState;
}
