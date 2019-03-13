package utils;

import enums.Json;
import spark.Request;

public class RequestUtils {

    private RequestUtils() {}

    public static <T> T getBodyParameter(Request request, String parameter) {
        try {
            return (T) Json.INSTANCE.requestToMap(request).get(parameter);
        } catch (Exception e) {
            return null;
        }
    }
}