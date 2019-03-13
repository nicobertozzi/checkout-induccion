package enums;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import errors.ErrorResponse;
import spark.Request;

import java.util.Map;
import java.util.TreeMap;

public enum Json {

    INSTANCE;

    public final ObjectMapper mapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public Map<String, Object> requestToMap(Request r) throws Exception {
        String body = r.body();
        if(body.length() == 0) {
            return new TreeMap<>();
        }
        return mapper.readValue(body, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * Method to deserialize String content to Object.
     */
    public <T> T mapTo(String str, Class<T> tClass) throws Exception {
        return mapper.readValue(str, tClass);
    }

    public String toJsonString(ErrorResponse errorResponse) {
        try {
            return mapper.writeValueAsString(errorResponse);
        } catch (JsonProcessingException e) {
            // Handle parsing manually.
            return "{ " +
                    "\"error\":\" " + errorResponse.getError() + " \", " +
                    "\"message\":\" " + errorResponse.getMessage() + " \" " +
                    "}";
        }
    }

}
