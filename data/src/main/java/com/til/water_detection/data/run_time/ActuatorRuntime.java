package com.til.water_detection.data.run_time;

import com.til.water_detection.data.Actuator;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActuatorRuntime {

    private boolean activated;
    private int embeddedId;
    private Actuator actuator;
}
