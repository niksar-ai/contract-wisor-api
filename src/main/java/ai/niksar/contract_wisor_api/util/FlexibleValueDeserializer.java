package ai.niksar.contract_wisor_api.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.List;

public class FlexibleValueDeserializer extends JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return switch (p.getCurrentToken()) {
            case VALUE_STRING -> p.getText();
            case VALUE_NUMBER_INT -> p.getDecimalValue();
            case VALUE_FALSE, VALUE_TRUE -> p.getBooleanValue();
            case START_ARRAY -> ctxt.readValue(p, List.class);
            default -> throw new IOException("Unsupported type");
        };
    }
}
