package com.til.water_detection.data;

import com.til.water_detection.data.state.DataState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmMessage {

    private long id;
    private String message;
    private DataState dataState;

}
