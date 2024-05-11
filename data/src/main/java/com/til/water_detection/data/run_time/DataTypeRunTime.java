package com.til.water_detection.data.run_time;

import com.til.water_detection.data.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataTypeRunTime {
    private float value;
    private int embeddedId;
    private DataType dataType;

}
