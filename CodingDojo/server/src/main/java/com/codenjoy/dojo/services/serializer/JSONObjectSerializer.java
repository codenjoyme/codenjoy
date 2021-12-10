package com.codenjoy.dojo.services.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.json.JSONObject;

import java.io.IOException;

public class JSONObjectSerializer extends StdSerializer<JSONObject> {

    public JSONObjectSerializer() {
        this(null);
    }

    public JSONObjectSerializer(Class<JSONObject> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(JSONObject value,
                          JsonGenerator generator,
                          SerializerProvider provider) throws IOException
    {
        generator.writeRawValue(value.toString());
    }
}
