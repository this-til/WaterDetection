package com.til.water_detection.data;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command {

    private int id;
    private int ruleId;
    private int actuatorId;
    private int commandTrigger;
    private float upper;
    private float low;


}
