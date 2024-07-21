import com.til.water_detection.script.IActuator
import com.til.water_detection.script.IDataType
import com.til.water_detection.script.IEquipment

static void main(IEquipment equipment) {
    IDataType type = equipment.getDataType("PH")
    IActuator actuator = equipment.getActuator("报警器")

    if (type == null || actuator == null) {
        print("PH or 报警器 is null")
    }
    if (!equipment.haveUpdate()) {
        return
    }
    if (type.getValue() > 8) {
        actuator.start()
    } else {
        actuator.stop()
    }
}

