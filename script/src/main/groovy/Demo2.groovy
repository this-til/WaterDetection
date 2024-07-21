import com.til.water_detection.script.IActuator
import com.til.water_detection.script.IEquipment

static void main(IEquipment equipment) {
    IActuator actuator = equipment.getActuator("LED");
    if (actuator != null) {
        if (actuator.isRun()) {
            actuator.stop();
        } else {
            actuator.start();
        }
    }
}

