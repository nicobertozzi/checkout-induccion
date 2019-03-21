package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public enum Json {

    INSTANCE;

    public final ObjectMapper mapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public String convertToJSON(String str) throws Exception {
        if(str.length() == 0) {
            return null;
        }

        if(isJSON(str)) {
            return  str;
        }

        Map<String, Object> map = asMap(str);

        return mapper.writeValueAsString(map);
        //return JsonUtils.toJson(map);
    }

    public <T> T convertToObject(String str, Class<T> tClass) throws Exception {
        if (str.length() == 0) {
            return null;
        }

        if(!isJSON(str)) {
            System.out.println("No es un JSON. Lo convertimos...");
            str = convertToJSON(str);
        }

        return mapper.readValue(str, tClass);
    }

    private static boolean isJSON(String json) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(json);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static Map<String, Object> asMap(String strEncoded) throws UnsupportedEncodingException {
        Map<String, Object> map = new LinkedHashMap<>();

        String strDecoded = URLDecoder.decode(strEncoded, StandardCharsets.UTF_8.name());
        for(String keyValue : strDecoded.trim().split("&")) {
            String[] tokens = keyValue.trim().split("=");
            String key = tokens[0];
            String value = tokens.length == 1 ? null : tokens[1];

            String[] keys = key.split("\\.");
            Map<String, Object> pointer = map;
            for(int i = 0; i < keys.length - 1; i++) {
                String currentKey = keys[i];
                Map<String, Object> nested = (Map<String, Object>) pointer.get(keys[i]);

                if(nested == null) {
                    nested = new LinkedHashMap<>();
                }

                pointer.put(currentKey, nested);
                pointer = nested;
            }

            pointer.put(keys[keys.length - 1], value);
        }

        return map;
    }

}
