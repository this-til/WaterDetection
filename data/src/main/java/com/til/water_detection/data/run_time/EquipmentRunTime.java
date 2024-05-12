package com.til.water_detection.data.run_time;

import com.til.water_detection.data.Equipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentRunTime {

    private Equipment equipment;
    private List<ActuatorRuntime> actuatorRuntimeList;
    private List<DataTypeRunTime> dataTypeRuntimeList;

}
