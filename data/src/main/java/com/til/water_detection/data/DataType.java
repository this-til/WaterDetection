package com.til.water_detection.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataType {

    private int id;
    private String name;

    private String suffix;
    private boolean percent;


}
