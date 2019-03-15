package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import errors.ErrorResponse;
import org.json.*;
import spark.Request;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

        // Si no tiene formato JSON, lo convertimos a pata...
        if(!isJSON(body)) {
            // lo decodificamos...
            body = URLDecoder.decode(body, StandardCharsets.UTF_8.name());

            JSONObject json = new JSONObject();
            for(String cadaBin : body.split("&")) {
                String[] valor = cadaBin.split("=");

                // Por si no estan presentes todos los valores...
                json.put(valor[0], valor.length > 1 ? valor[1] : "");
            }

            body = json.toString();
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

    public static boolean isJSON(String json) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(json);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
