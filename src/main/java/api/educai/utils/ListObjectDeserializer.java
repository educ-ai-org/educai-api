package api.educai.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ListObjectDeserializer extends JsonDeserializer<ListObject<?>> {
    @Override
    public ListObject<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ListObject<Object> listObject = new ListObject<>(5);
        while (p.nextToken() != JsonToken.END_ARRAY) {
            listObject.add(p.readValueAs(Object.class));
        }
        return listObject;
    }
}
