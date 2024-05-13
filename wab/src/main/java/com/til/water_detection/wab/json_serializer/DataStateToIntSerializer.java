package com.til.water_detection.wab.json_serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.til.water_detection.data.state.DataState;

import java.io.IOException;

public class DataStateToIntSerializer extends JsonSerializer<DataState> {

    @Override
    public void serialize(DataState value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        gen.writeNumber(value.getState());
    }
}
