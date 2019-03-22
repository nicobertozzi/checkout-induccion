package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import spark.Request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class RequestUtil {

    private RequestUtil() {}

    public static String getBodyAsJSON(Request request) throws UnsupportedEncodingException, JsonProcessingException {
        return Json.INSTANCE.convertToJSON(request.body());
    }

    public static <T> T getBodyAsObject(Request request, Class<T> tClass) throws IOException {
        return Json.INSTANCE.convertToObject(request.body(), tClass);
    }

}