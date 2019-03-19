package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class RequestUtil {

    private RequestUtil() {}

    public static String getBodyAsJSON(Request request) {
        try {
            String body = request.body();
            if(body.length() == 0) {
                return "{}";
            }

            return isJSON(body) ? body : convertToJSON(body);
        } catch (Exception e) {
            return null;
        }
    }

    public static <K, V> Map<K, V> getBodyAsMap(Request request) {
        try {
            String body = request.body();
            if(body.length() == 0) {
                return new TreeMap<>();
            }

            if(!isJSON(body)) {
                System.out.println("No es un JSON. Lo convertimos...");
                body = convertToJSON(body);
            }

            return JsonUtils.fromJson(body, Map.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getBodyParameter(Request request, T parameter) {
        try {
            return (T) getBodyAsMap(request).get(parameter);
        } catch (Exception e) {
            return null;
        }
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

    private static String convertToJSON(String strEncoded) {
        try {
            Map<String, Object> map = asMap(strEncoded);

            return JsonUtils.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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