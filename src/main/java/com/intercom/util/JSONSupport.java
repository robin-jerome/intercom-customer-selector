package com.intercom.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.intercom.model.JSONParsingException;

import java.io.IOException;

public final class JSONSupport {

    // Private constructor as this is a static factory utility
    private JSONSupport() {
        // Prevent creation through reflection or serialization/deserialization
        throw new AssertionError("Instances of static factory are not supposed to be instantiated");
    }

    private static final ObjectMapper FAIL_ON_UNKNOWN_MAPPER = new ObjectMapper()
            .registerModule(new JSR310Module())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true); // Unknown fields in JSON throw exception

    public static <T> T fromJSON(String json, Class<T> clazz) {
        try {
            return FAIL_ON_UNKNOWN_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new JSONParsingException(e.getMessage(), e);
        }
    }
}
