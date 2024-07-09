import com.til.water_detection.script.DataState
import com.til.water_detection.script.IActuator
import com.til.water_detection.script.IDataType
import com.til.water_detection.script.IEquipment

static void main(IEquipment equipment) {
    for (final def d in equipment.getDataTypes()) {
        d.setDataState(DataState.EXCEPTION_LOWER)
    }
    /*if (!equipment.haveUpdate()) {
        return
    }
    boolean hasException = false;
    for (final IDataType d in equipment.getDataTypes()) {
        if (d.getValue() > 200) {
            d.setDataState(DataState.EXCEPTION_UPPER)
            hasException = true;
        } else if (d.getValue() > 100) {
            d.setDataState(DataState.WARN_UPPER)
        } else if (d.getValue() < 0) {
            d.setDataState(DataState.WARN_LOWER)
        } else if (d.getValue() < -100) {
            d.setDataState(DataState.EXCEPTION_LOWER)
            hasException = true;
        } else {
            d.setDataState(DataState.NORMAL)
        }
    }
    if (hasException) {
        print("hasException")
        IActuator actuator = equipment.getActuator("LED")
        if (actuator != null) {
            if (actuator.isRun()) {
                actuator.stop()
            } else {
                actuator.start()
            }
        }
    }*/
}

