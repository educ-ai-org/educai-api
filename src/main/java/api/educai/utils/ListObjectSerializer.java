package api.educai.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ListObjectSerializer extends JsonSerializer<ListObject<?>> {
    @Override
    public void serialize(ListObject<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for (int i = 0; i < value.getSize(); i++) {
            gen.writeObject(value.getElement(i));
        }
        gen.writeEndArray();
    }
}