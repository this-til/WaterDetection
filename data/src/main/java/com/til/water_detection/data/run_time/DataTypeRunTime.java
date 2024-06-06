package com.til.water_detection.data.run_time;

import com.til.water_detection.data.DataType;
import com.til.water_detection.data.Rule;
import com.til.water_detection.data.state.DataState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.Queue;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataTypeRunTime {
    private float value;
    private DataState dataState;

    private int embeddedId;
    private DataType dataType;
    private Rule rule;

}
