import com.til.water_detection.script.DataState
import com.til.water_detection.script.IDataType
import com.til.water_detection.script.IEquipment

static void main(IEquipment equipment) {

    if (!equipment.haveUpdate()) {
        return
    }

     for (final IDataType d in equipment.getDataTypes()) {
         if (d.getValue() > 200) {
             d.setDataState(DataState.EXCEPTION_UPPER)
         } else if (d.getValue() > 100) {
             d.setDataState(DataState.WARN_UPPER)
         } else if (d.getValue() < 0) {
             d.setDataState(DataState.WARN_LOWER)
         } else if (d.getValue() < -100) {
             d.setDataState(DataState.EXCEPTION_LOWER)
         } else {
             d.setDataState(DataState.NORMAL)
         }
     }

    /*   for (final def a in equipment.getActuators()) {
           a.start()
       }*/

/*
    for (final def a in equipment.getActuators()) {
        if (a.isRun()) {
            a.stop()
        } else {
            a.start()
        }
    }
*/

}

