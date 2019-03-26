package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public enum Json {

    INSTANCE;

    public final ObjectMapper mapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public String convertToJSON(String str) throws UnsupportedEncodingException, JsonProcessingException {
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

    public <T> T convertToObject(String str, Class<T> tClass) throws IOException {
        if (str.length() == 0) {
            return null;
        }

        if(!isJSON(str)) {
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
        System.out.println(strDecoded);
        for(String keyValue : strDecoded.trim().split("&")) {
            String[] tokens = keyValue.trim().split("=");
            String key = tokens[0];
            String value = tokens.length == 1 ? null : tokens[1];

            String[] keys = key.split("\\.");
            Map<String, Object> pointer = map;
            for(int i = 0; i < keys.length - 1; i++) {
                String currentKey = keys[i];

                Map<String, Object> nested = (Map<String, Object>) pointer.get(currentKey);

                if(nested == null) {
                    nested = new LinkedHashMap<>();
                }

                pointer.put(currentKey, nested);
                pointer = nested;
            }

            pointer.put(keys[keys.length - 1], value);
        }

        // Para los atributos que son arrays, hacemos la conversion manual...
        Map<String, Object> mapToAdd = new LinkedHashMap<>();
        List<String> listToRemove = new ArrayList();
        for(String key : map.keySet()) {
            if(key.contains("[")) {
                String prefix = key.substring(0, key.indexOf("["));

                List lista = (List) mapToAdd.get(prefix);
                if(lista == null) {
                    lista = new ArrayList();
                }

                Map<String, Object> objArray = (Map<String, Object>) map.get(key);
                lista.add(objArray);
                mapToAdd.put(prefix, lista);

                listToRemove.add(key);
            }
        }

        // Eliminamos las key de los elementos del array
        for(String keyRemoved : listToRemove) {
            map.remove(keyRemoved);
        }
        // Agregamos las key con sus arrays
        for(String keyAdded : mapToAdd.keySet()) {
            map.put(keyAdded, mapToAdd.get(keyAdded));
        }
        //

        return map;
    }

}
